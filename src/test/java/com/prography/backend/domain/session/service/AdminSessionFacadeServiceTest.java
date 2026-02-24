package com.prography.backend.domain.session.service;

import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.attendance.service.AttendanceService;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.repository.QrCodeRepository;
import com.prography.backend.domain.session.dto.request.AdminSessionCreateRequest;
import com.prography.backend.domain.session.dto.request.AdminSessionUpdateRequest;
import com.prography.backend.domain.session.dto.response.AdminSessionResponse;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.session.service<br>
 * fileName       : AdminSessionFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 일정 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class AdminSessionFacadeServiceTest {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @Autowired
    private AdminSessionFacadeService adminSessionFacadeService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createSession_createsQrCode() {
        // Given
        createCurrentCohort();
        AdminSessionCreateRequest request = new AdminSessionCreateRequest();
        setField(request, "title", "오리엔테이션");
        setField(request, "date", LocalDate.of(2026, 2, 26));
        setField(request, "time", LocalTime.of(19, 0));
        setField(request, "location", "강남");

        // When
        AdminSessionResponse response = adminSessionFacadeService.createSession(request);

        // Then
        assertThat(response.isQrActive()).isTrue();
        assertThat(qrCodeRepository.findFirstBySessionIdAndExpiresAtAfter(response.getId(), now())).isPresent();
    }

    @Test
    void getSessions_includesAttendanceSummaryAndQrActive() {
        // Given
        CohortEntity cohort = createCurrentCohort();
        SessionEntity session = createSession(cohort, SessionStatus.SCHEDULED);
        MemberEntity member1 = createMember("session-list-1");
        MemberEntity member2 = createMember("session-list-2");

        attendanceService.create(session, member1, AttendanceStatus.PRESENT, null, 0L, null, now(), null);
        attendanceService.create(session, member2, AttendanceStatus.LATE, 5, 2_500L, null, now(), null);
        qrCodeRepository.save(QrCodeEntity.builder()
                .session(session)
                .hashValue("active-qr-list")
                .expiresAt(now().plusHours(1))
                .build());

        // When
        List<AdminSessionResponse> responses = adminSessionFacadeService.getSessions(null, null, null);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).isQrActive()).isTrue();
        assertThat(responses.get(0).getAttendanceSummary().getPresent()).isEqualTo(1);
        assertThat(responses.get(0).getAttendanceSummary().getLate()).isEqualTo(1);
        assertThat(responses.get(0).getAttendanceSummary().getTotal()).isEqualTo(2);
    }

    @Test
    void updateSession_cancelledSession_throwsException() {
        // Given
        CohortEntity cohort = createCurrentCohort();
        SessionEntity cancelled = createSession(cohort, SessionStatus.CANCELLED);
        AdminSessionUpdateRequest request = new AdminSessionUpdateRequest();
        setField(request, "title", "변경된 제목");
        setField(request, "date", null);
        setField(request, "time", null);
        setField(request, "location", null);
        setField(request, "status", SessionStatus.SCHEDULED);

        // When & Then
        assertThatThrownBy(() -> adminSessionFacadeService.updateSession(cancelled.getId(), request))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.SESSION_ALREADY_CANCELLED);
    }

    @Test
    void cancelSession_setsCancelledAndQrInactive() {
        // Given
        CohortEntity cohort = createCurrentCohort();
        SessionEntity session = createSession(cohort, SessionStatus.SCHEDULED);
        qrCodeRepository.save(QrCodeEntity.builder()
                .session(session)
                .hashValue("active-qr-cancel")
                .expiresAt(now().plusHours(2))
                .build());

        // When
        AdminSessionResponse response = adminSessionFacadeService.cancelSession(session.getId());

        // Then
        assertThat(response.getStatus()).isEqualTo(SessionStatus.CANCELLED);
        assertThat(response.isQrActive()).isFalse();
        assertThat(qrCodeRepository.findFirstBySessionIdAndExpiresAtAfter(session.getId(), now())).isNotPresent();
    }

    private CohortEntity createCurrentCohort() {
        return cohortRepository.save(CohortEntity.builder()
                .generation(PolicyConstants.CURRENT_GENERATION)
                .name("11기")
                .build());
    }

    private SessionEntity createSession(CohortEntity cohort, SessionStatus status) {
        return sessionRepository.save(SessionEntity.builder()
                .cohort(cohort)
                .title("정기 모임")
                .date(LocalDate.of(2026, 2, 25))
                .time(LocalTime.of(10, 0))
                .location("강남")
                .status(status)
                .build());
    }

    private MemberEntity createMember(String loginId) {
        return memberService.createMember(
                loginId,
                passwordEncoder.encode("password123"),
                "홍길동",
                "010-1234-5678",
                MemberRole.MEMBER
        );
    }

    private LocalDateTime now() {
        return LocalDateTime.now(KOREA_ZONE);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
