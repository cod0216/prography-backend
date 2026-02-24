package com.prography.backend.domain.attendance.service;

import com.prography.backend.domain.attendance.dto.request.AdminAttendanceRegisterRequest;
import com.prography.backend.domain.attendance.dto.request.AdminAttendanceUpdateRequest;
import com.prography.backend.domain.attendance.dto.response.AdminMemberAttendanceDetailResponse;
import com.prography.backend.domain.attendance.dto.response.AdminSessionAttendanceSummaryResponse;
import com.prography.backend.domain.attendance.dto.response.AdminSessionAttendancesResponse;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.attendance.mapper.AttendanceMapper;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.service.CohortService;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.service.DepositHistoryService;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.service.SessionService;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.domain.attendance.service<br>
 * fileName       : AdminAttendanceFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 출결 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             QR 코드 없는 수동 출결 등록 처리<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminAttendanceFacadeService {

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;
    private final SessionService sessionService;
    private final MemberService memberService;
    private final CohortService cohortService;
    private final CohortMemberService cohortMemberService;
    private final DepositHistoryService depositHistoryService;

    public AttendanceResponse registerAttendance(AdminAttendanceRegisterRequest request) {
        SessionEntity session = sessionService.getById(request.getSessionId());
        MemberEntity member = memberService.getById(request.getMemberId());

        if (attendanceService.existsBySessionAndMember(session.getId(), member.getId())) {
            throw new CustomException(StatusCode.ATTENDANCE_ALREADY_CHECKED);
        }

        CohortMemberEntity cohortMember = getCurrentCohortMember(member.getId());
        AttendanceStatus status = request.getStatus();
        Integer lateMinutes = normalizeLateMinutes(status, request.getLateMinutes());

        if (status == AttendanceStatus.EXCUSED) {
            validateExcuseLimit(cohortMember.getExcuseCount());
            cohortMember.updateExcuseCount(cohortMember.getExcuseCount() + 1);
        }

        long penaltyAmount = calculatePenalty(status, lateMinutes);
        AttendanceEntity attendance = attendanceService.create(
                session,
                member,
                status,
                lateMinutes,
                penaltyAmount,
                request.getReason(),
                null,
                null
        );

        if (penaltyAmount > 0) {
            applyPenalty(cohortMember, penaltyAmount, attendance.getId(), "출결 등록 - " + status + " 패널티 " + penaltyAmount + "원");
        }

        return attendanceMapper.toResponse(attendance);
    }

    public AttendanceResponse updateAttendance(Long attendanceId, AdminAttendanceUpdateRequest request) {
        AttendanceEntity attendance = attendanceService.getById(attendanceId);
        CohortMemberEntity cohortMember = getCurrentCohortMember(attendance.getMember().getId());

        AttendanceStatus oldStatus = attendance.getStatus();
        AttendanceStatus newStatus = request.getStatus();
        Integer lateMinutes = normalizeLateMinutes(newStatus, request.getLateMinutes());

        long oldPenalty = attendance.getPenaltyAmount();
        long newPenalty = calculatePenalty(newStatus, lateMinutes);
        long diff = newPenalty - oldPenalty;

        adjustExcuseCount(cohortMember, oldStatus, newStatus);
        applyDepositDiff(cohortMember, diff, attendance.getId(), newStatus);

        attendance.updateByAdmin(newStatus, lateMinutes, newPenalty, request.getReason());
        return attendanceMapper.toResponse(attendance);
    }

    @Transactional(readOnly = true)
    public List<AdminSessionAttendanceSummaryResponse> getSessionSummary(Long sessionId) {
        sessionService.getById(sessionId);

        List<CohortMemberEntity> cohortMembers = cohortMemberService.getByGeneration(PolicyConstants.CURRENT_GENERATION);

        List<Long> memberIds = cohortMembers.stream()
                .map(cohortMember -> cohortMember.getMember().getId())
                .toList();

        Map<Long, List<AttendanceEntity>> attendanceMap = attendanceService.getByMemberIds(memberIds).stream()
                .collect(Collectors.groupingBy(attendance -> attendance.getMember().getId()));

        List<AdminSessionAttendanceSummaryResponse> results = new ArrayList<>();
        for (CohortMemberEntity cohortMember : cohortMembers) {
            Long memberId = cohortMember.getMember().getId();
            List<AttendanceEntity> attendances = attendanceMap.getOrDefault(memberId, List.of());

            int present = countStatus(attendances, AttendanceStatus.PRESENT);
            int absent = countStatus(attendances, AttendanceStatus.ABSENT);
            int late = countStatus(attendances, AttendanceStatus.LATE);
            int excused = countStatus(attendances, AttendanceStatus.EXCUSED);
            long totalPenalty = attendances.stream()
                    .mapToLong(AttendanceEntity::getPenaltyAmount)
                    .sum();

            results.add(AdminSessionAttendanceSummaryResponse.builder()
                    .memberId(memberId)
                    .memberName(cohortMember.getMember().getName())
                    .present(present)
                    .absent(absent)
                    .late(late)
                    .excused(excused)
                    .totalPenalty(totalPenalty)
                    .deposit(cohortMember.getDeposit())
                    .build());
        }

        return results;
    }

    @Transactional(readOnly = true)
    public AdminMemberAttendanceDetailResponse getMemberAttendanceDetail(Long memberId) {
        MemberEntity member = memberService.getById(memberId);
        List<AttendanceResponse> attendances = attendanceService.getByMemberId(memberId).stream()
                .map(attendanceMapper::toResponse)
                .toList();

        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        Optional<CohortMemberEntity> cohortMember = cohortMemberService.findByMemberAndCohort(memberId, cohort.getId());

        PartEntity part = cohortMember.map(CohortMemberEntity::getPart).orElse(null);
        TeamEntity team = cohortMember.map(CohortMemberEntity::getTeam).orElse(null);

        return AdminMemberAttendanceDetailResponse.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .generation(cohortMember.map(cm -> cm.getCohort().getGeneration()).orElse(null))
                .partName(part == null ? null : part.getName())
                .teamName(team == null ? null : team.getName())
                .deposit(cohortMember.map(CohortMemberEntity::getDeposit).orElse(null))
                .excuseCount(cohortMember.map(CohortMemberEntity::getExcuseCount).orElse(null))
                .attendances(attendances)
                .build();
    }

    @Transactional(readOnly = true)
    public AdminSessionAttendancesResponse getSessionAttendances(Long sessionId) {
        SessionEntity session = sessionService.getById(sessionId);
        List<AttendanceResponse> attendances = attendanceService.getBySessionId(sessionId).stream()
                .map(attendanceMapper::toResponse)
                .toList();

        return AdminSessionAttendancesResponse.builder()
                .sessionId(session.getId())
                .sessionTitle(session.getTitle())
                .attendances(attendances)
                .build();
    }

    private CohortMemberEntity getCurrentCohortMember(Long memberId) {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        return cohortMemberService.findByMemberAndCohort(memberId, cohort.getId())
                .orElseThrow(() -> new CustomException(StatusCode.COHORT_MEMBER_NOT_FOUND));
    }

    private Integer normalizeLateMinutes(AttendanceStatus status, Integer lateMinutes) {
        if (status == AttendanceStatus.LATE) {
            if (lateMinutes == null) {
                throw new CustomException(StatusCode.INVALID_INPUT);
            }
            if (lateMinutes < 0) {
                throw new CustomException(StatusCode.INVALID_INPUT);
            }
            return lateMinutes;
        }
        return null;
    }

    private long calculatePenalty(AttendanceStatus status, Integer lateMinutes) {
        return switch (status) {
            case PRESENT, EXCUSED -> 0;
            case ABSENT -> PolicyConstants.PENALTY_ABSENT;
            case LATE -> Math.min((long) lateMinutes * PolicyConstants.PENALTY_LATE_PER_MINUTE, PolicyConstants.PENALTY_MAX);
        };
    }

    private void validateExcuseLimit(int excuseCount) {
        if (excuseCount >= PolicyConstants.EXCUSE_LIMIT) {
            throw new CustomException(StatusCode.EXCUSE_LIMIT_EXCEEDED);
        }
    }

    private void adjustExcuseCount(CohortMemberEntity cohortMember, AttendanceStatus oldStatus, AttendanceStatus newStatus) {
        if (oldStatus != AttendanceStatus.EXCUSED && newStatus == AttendanceStatus.EXCUSED) {
            validateExcuseLimit(cohortMember.getExcuseCount());
            cohortMember.updateExcuseCount(cohortMember.getExcuseCount() + 1);
        } else if (oldStatus == AttendanceStatus.EXCUSED && newStatus != AttendanceStatus.EXCUSED) {
            cohortMember.updateExcuseCount(Math.max(0, cohortMember.getExcuseCount() - 1));
        }
    }

    private void applyDepositDiff(CohortMemberEntity cohortMember, long diff, Long attendanceId, AttendanceStatus status) {
        if (diff == 0) {
            return;
        }

        if (diff > 0) {
            applyPenalty(cohortMember, diff, attendanceId, "출결 수정 - " + status + " 패널티 " + diff + "원");
            return;
        }

        long refundAmount = Math.abs(diff);
        long newDeposit = cohortMember.getDeposit() + refundAmount;
        cohortMember.updateDeposit(newDeposit);
        depositHistoryService.createRefund(cohortMember, refundAmount, attendanceId, "출결 수정 - 환급 " + refundAmount + "원");
    }

    private void applyPenalty(CohortMemberEntity cohortMember, long penaltyAmount, Long attendanceId, String description) {
        if (cohortMember.getDeposit() < penaltyAmount) {
            throw new CustomException(StatusCode.DEPOSIT_INSUFFICIENT);
        }

        long newDeposit = cohortMember.getDeposit() - penaltyAmount;
        cohortMember.updateDeposit(newDeposit);
        depositHistoryService.createPenalty(cohortMember, penaltyAmount, attendanceId, description);
    }

    private int countStatus(List<AttendanceEntity> attendances, AttendanceStatus status) {
        return (int) attendances.stream()
                .filter(attendance -> attendance.getStatus() == status)
                .count();
    }
}
