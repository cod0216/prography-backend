package com.prography.backend.domain.attendance.service;

import com.prography.backend.domain.attendance.dto.request.MemberAttendanceCheckInRequest;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceSummaryResponse;
import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import com.prography.backend.domain.deposit.entity.DepositType;
import com.prography.backend.domain.deposit.repository.DepositHistoryRepository;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.repository.QrCodeRepository;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.PolicyConstants;
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

/**
 * packageName    : com.prography.backend.domain.attendance.service<br>
 * fileName       : MemberAttendanceFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 출결 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class MemberAttendanceFacadeServiceTest {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @Autowired
    private MemberAttendanceFacadeService memberAttendanceFacadeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CohortMemberService cohortMemberService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Test
    void checkIn_late_createsPenaltyHistory() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("late-user", PolicyConstants.INITIAL_DEPOSIT);
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);
        SessionEntity session = createSession(cohortMember.getCohort(), now.toLocalDate(), now.minusMinutes(10).toLocalTime(), SessionStatus.IN_PROGRESS);
        QrCodeEntity qrCode = qrCodeRepository.save(QrCodeEntity.builder()
                .session(session)
                .hashValue("hash-late")
                .expiresAt(now.plusHours(1))
                .build());

        MemberAttendanceCheckInRequest request = new MemberAttendanceCheckInRequest();
        setField(request, "hashValue", qrCode.getHashValue());
        setField(request, "memberId", cohortMember.getMember().getId());

        // When
        AttendanceResponse response = memberAttendanceFacadeService.checkIn(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(AttendanceStatus.LATE);
        assertThat(response.getPenaltyAmount()).isPositive();

        CohortMemberEntity updated = cohortMemberService.getById(cohortMember.getId());
        assertThat(updated.getDeposit()).isEqualTo(PolicyConstants.INITIAL_DEPOSIT - response.getPenaltyAmount());

        List<DepositHistoryEntity> histories = depositHistoryRepository.findByCohortMemberIdOrderByCreatedAtAsc(cohortMember.getId());
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getType()).isEqualTo(DepositType.PENALTY);
        assertThat(histories.get(0).getAttendanceId()).isEqualTo(response.getId());
    }

    @Test
    void getAttendances_returnsSessionTitle() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("list-user", 90_000L);
        SessionEntity session = createSession(cohortMember.getCohort(), LocalDate.of(2026, 2, 24), LocalTime.of(10, 0), SessionStatus.SCHEDULED);
        attendanceService.create(session, cohortMember.getMember(), AttendanceStatus.PRESENT, null, 0L, null, null, null);

        // When
        List<MemberAttendanceResponse> responses = memberAttendanceFacadeService.getAttendances(cohortMember.getMember().getId());

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getSessionTitle()).isEqualTo(session.getTitle());
    }

    @Test
    void getAttendanceSummary_countsAndDeposit() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("summary-user", 85_000L);
        SessionEntity session1 = createSession(cohortMember.getCohort(), LocalDate.of(2026, 2, 24), LocalTime.of(10, 0), SessionStatus.SCHEDULED);
        SessionEntity session2 = createSession(cohortMember.getCohort(), LocalDate.of(2026, 2, 25), LocalTime.of(10, 0), SessionStatus.SCHEDULED);
        SessionEntity session3 = createSession(cohortMember.getCohort(), LocalDate.of(2026, 2, 26), LocalTime.of(10, 0), SessionStatus.SCHEDULED);

        attendanceService.create(session1, cohortMember.getMember(), AttendanceStatus.PRESENT, null, 0L, null, null, null);
        attendanceService.create(session2, cohortMember.getMember(), AttendanceStatus.ABSENT, null, PolicyConstants.PENALTY_ABSENT, null, null, null);
        attendanceService.create(session3, cohortMember.getMember(), AttendanceStatus.LATE, 5, 2_500L, null, null, null);

        // When
        MemberAttendanceSummaryResponse response = memberAttendanceFacadeService.getAttendanceSummary(cohortMember.getMember().getId());

        // Then
        assertThat(response.getPresent()).isEqualTo(1);
        assertThat(response.getAbsent()).isEqualTo(1);
        assertThat(response.getLate()).isEqualTo(1);
        assertThat(response.getExcused()).isZero();
        assertThat(response.getTotalPenalty()).isEqualTo(PolicyConstants.PENALTY_ABSENT + 2_500L);
        assertThat(response.getDeposit()).isEqualTo(85_000L);
    }

    private CohortMemberEntity createCohortMember(String loginId, long deposit) {
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(PolicyConstants.CURRENT_GENERATION).name("11기").build());
        MemberEntity member = memberService.createMember(
                loginId,
                passwordEncoder.encode("password123"),
                "홍길동",
                "010-1234-5678",
                MemberRole.MEMBER
        );
        return cohortMemberService.create(member, cohort, null, null, deposit);
    }

    private SessionEntity createSession(CohortEntity cohort, LocalDate date, LocalTime time, SessionStatus status) {
        return sessionRepository.save(SessionEntity.builder()
                .cohort(cohort)
                .title("정기 모임")
                .date(date)
                .time(time)
                .location("강남")
                .status(status)
                .build());
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
