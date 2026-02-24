package com.prography.backend.domain.attendance.service;

import com.prography.backend.domain.attendance.dto.request.MemberAttendanceCheckInRequest;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceSummaryResponse;
import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.attendance.mapper.AttendanceMapper;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.service.CohortService;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.service.DepositHistoryService;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberStatus;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.service.QrCodeService;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.service.SessionService;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.attendance.service<br>
 * fileName       : MemberAttendanceFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 출결 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAttendanceFacadeService {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;
    private final SessionService sessionService;
    private final MemberService memberService;
    private final CohortService cohortService;
    private final CohortMemberService cohortMemberService;
    private final DepositHistoryService depositHistoryService;
    private final QrCodeService qrCodeService;

    public AttendanceResponse checkIn(MemberAttendanceCheckInRequest request) {
        QrCodeEntity qrCode = qrCodeService.getByHashValue(request.getHashValue());
        validateQrNotExpired(qrCode);

        SessionEntity session = sessionService.getById(qrCode.getSession().getId());
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new CustomException(StatusCode.SESSION_NOT_IN_PROGRESS);
        }

        MemberEntity member = memberService.getById(request.getMemberId());
        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new CustomException(StatusCode.MEMBER_WITHDRAWN);
        }

        if (attendanceService.existsBySessionAndMember(session.getId(), member.getId())) {
            throw new CustomException(StatusCode.ATTENDANCE_ALREADY_CHECKED);
        }

        CohortMemberEntity cohortMember = getCurrentCohortMember(member.getId());

        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);
        LocalDateTime sessionStart = LocalDateTime.of(session.getDate(), session.getTime());
        AttendanceStatus status = now.isAfter(sessionStart) ? AttendanceStatus.LATE : AttendanceStatus.PRESENT;
        Integer lateMinutes = status == AttendanceStatus.LATE ? (int) Math.max(0, Duration.between(sessionStart, now).toMinutes()) : null;
        long penaltyAmount = calculatePenalty(status, lateMinutes);

        AttendanceEntity attendance = attendanceService.create(
                session,
                member,
                status,
                lateMinutes,
                penaltyAmount,
                null,
                now,
                qrCode.getId()
        );

        if (penaltyAmount > 0) {
            applyPenalty(cohortMember, penaltyAmount, attendance.getId(), "QR 출석 - LATE 패널티 " + penaltyAmount + "원");
        }

        return attendanceMapper.toResponse(attendance);
    }

    @Transactional(readOnly = true)
    public List<MemberAttendanceResponse> getAttendances(Long memberId) {
        memberService.getById(memberId);
        return attendanceService.getByMemberId(memberId).stream()
                .map(attendanceMapper::toMemberResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberAttendanceSummaryResponse getAttendanceSummary(Long memberId) {
        memberService.getById(memberId);
        List<AttendanceEntity> attendances = attendanceService.getByMemberId(memberId);

        int present = countStatus(attendances, AttendanceStatus.PRESENT);
        int absent = countStatus(attendances, AttendanceStatus.ABSENT);
        int late = countStatus(attendances, AttendanceStatus.LATE);
        int excused = countStatus(attendances, AttendanceStatus.EXCUSED);
        long totalPenalty = attendances.stream()
                .mapToLong(AttendanceEntity::getPenaltyAmount)
                .sum();

        Long deposit = getCurrentCohortMemberOptional(memberId)
                .map(CohortMemberEntity::getDeposit)
                .orElse(null);

        return MemberAttendanceSummaryResponse.builder()
                .memberId(memberId)
                .present(present)
                .absent(absent)
                .late(late)
                .excused(excused)
                .totalPenalty(totalPenalty)
                .deposit(deposit)
                .build();
    }

    private void validateQrNotExpired(QrCodeEntity qrCode) {
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);
        if (qrCode.getExpiresAt().isBefore(now)) {
            throw new CustomException(StatusCode.QR_EXPIRED);
        }
    }

    private CohortMemberEntity getCurrentCohortMember(Long memberId) {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        return cohortMemberService.findByMemberAndCohort(memberId, cohort.getId())
                .orElseThrow(() -> new CustomException(StatusCode.COHORT_MEMBER_NOT_FOUND));
    }

    private Optional<CohortMemberEntity> getCurrentCohortMemberOptional(Long memberId) {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        return cohortMemberService.findByMemberAndCohort(memberId, cohort.getId());
    }

    private long calculatePenalty(AttendanceStatus status, Integer lateMinutes) {
        return switch (status) {
            case PRESENT, EXCUSED -> 0;
            case ABSENT -> PolicyConstants.PENALTY_ABSENT;
            case LATE -> Math.min((long) lateMinutes * PolicyConstants.PENALTY_LATE_PER_MINUTE, PolicyConstants.PENALTY_MAX);
        };
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
