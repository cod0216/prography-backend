package com.prography.backend.domain.session.service;

import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.attendance.service.AttendanceService;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.service.CohortService;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.service.QrCodeService;
import com.prography.backend.domain.session.dto.request.AdminSessionCreateRequest;
import com.prography.backend.domain.session.dto.request.AdminSessionUpdateRequest;
import com.prography.backend.domain.session.dto.response.AdminSessionResponse;
import com.prography.backend.domain.session.dto.response.SessionAttendanceSummaryResponse;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.mapper.SessionMapper;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.domain.session.service<br>
 * fileName       : AdminSessionFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminSessionFacadeService {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final SessionService sessionService;
    private final SessionMapper sessionMapper;
    private final CohortService cohortService;
    private final AttendanceService attendanceService;
    private final QrCodeService qrCodeService;

    @Transactional(readOnly = true)
    public List<AdminSessionResponse> getSessions(LocalDate dateFrom, LocalDate dateTo, SessionStatus status) {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        List<SessionEntity> sessions = sessionService.getByCohortId(cohort.getId());

        List<SessionEntity> filtered = sessions.stream()
                .filter(session -> matchesDateFrom(session, dateFrom))
                .filter(session -> matchesDateTo(session, dateTo))
                .filter(session -> status == null || session.getStatus() == status)
                .toList();

        List<Long> sessionIds = filtered.stream()
                .map(SessionEntity::getId)
                .toList();

        Map<Long, List<AttendanceEntity>> attendanceMap = attendanceService.getBySessionIds(sessionIds).stream()
                .collect(Collectors.groupingBy(attendance -> attendance.getSession().getId()));

        Set<Long> activeQrSessionIds = qrCodeService.findActiveBySessionIds(sessionIds, now())
                .stream()
                .map(qrCode -> qrCode.getSession().getId())
                .collect(Collectors.toSet());

        List<AdminSessionResponse> responses = new ArrayList<>();
        for (SessionEntity session : filtered) {
            List<AttendanceEntity> attendances = attendanceMap.getOrDefault(session.getId(), List.of());
            SessionAttendanceSummaryResponse summary = buildSummary(attendances);
            boolean qrActive = activeQrSessionIds.contains(session.getId());
            responses.add(sessionMapper.toAdminResponse(session, summary, qrActive));
        }

        return responses;
    }

    public AdminSessionResponse createSession(AdminSessionCreateRequest request) {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        SessionEntity session = sessionService.create(cohort, request.getTitle(), request.getDate(), request.getTime(), request.getLocation());

        LocalDateTime expiresAt = now().plusHours(PolicyConstants.QR_EXPIRE_HOURS);
        qrCodeService.create(session, expiresAt);

        SessionAttendanceSummaryResponse summary = buildSummary(List.of());
        return sessionMapper.toAdminResponse(session, summary, true);
    }

    public AdminSessionResponse updateSession(Long sessionId, AdminSessionUpdateRequest request) {
        SessionEntity session = sessionService.getById(sessionId);
        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new CustomException(StatusCode.SESSION_ALREADY_CANCELLED);
        }

        sessionService.update(session, request.getTitle(), request.getDate(), request.getTime(), request.getLocation(), request.getStatus());

        if (session.getStatus() == SessionStatus.CANCELLED) {
            expireActiveQr(session.getId());
        }

        SessionAttendanceSummaryResponse summary = buildSummary(attendanceService.getBySessionId(session.getId()));
        boolean qrActive = qrCodeService.findActiveBySessionId(session.getId(), now()).isPresent();
        return sessionMapper.toAdminResponse(session, summary, qrActive);
    }

    public AdminSessionResponse cancelSession(Long sessionId) {
        SessionEntity session = sessionService.getById(sessionId);
        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new CustomException(StatusCode.SESSION_ALREADY_CANCELLED);
        }

        sessionService.cancel(session);
        expireActiveQr(session.getId());

        SessionAttendanceSummaryResponse summary = buildSummary(attendanceService.getBySessionId(session.getId()));
        return sessionMapper.toAdminResponse(session, summary, false);
    }

    private void expireActiveQr(Long sessionId) {
        qrCodeService.findActiveBySessionId(sessionId, now())
                .ifPresent(qrCode -> qrCodeService.expire(qrCode, now()));
    }

    private SessionAttendanceSummaryResponse buildSummary(List<AttendanceEntity> attendances) {
        int present = countStatus(attendances, AttendanceStatus.PRESENT);
        int absent = countStatus(attendances, AttendanceStatus.ABSENT);
        int late = countStatus(attendances, AttendanceStatus.LATE);
        int excused = countStatus(attendances, AttendanceStatus.EXCUSED);
        int total = present + absent + late + excused;

        return SessionAttendanceSummaryResponse.builder()
                .present(present)
                .absent(absent)
                .late(late)
                .excused(excused)
                .total(total)
                .build();
    }

    private int countStatus(List<AttendanceEntity> attendances, AttendanceStatus status) {
        return (int) attendances.stream()
                .filter(attendance -> attendance.getStatus() == status)
                .count();
    }

    private boolean matchesDateFrom(SessionEntity session, LocalDate dateFrom) {
        if (dateFrom == null) {
            return true;
        }
        return !session.getDate().isBefore(dateFrom);
    }

    private boolean matchesDateTo(SessionEntity session, LocalDate dateTo) {
        if (dateTo == null) {
            return true;
        }
        return !session.getDate().isAfter(dateTo);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(KOREA_ZONE);
    }
}
