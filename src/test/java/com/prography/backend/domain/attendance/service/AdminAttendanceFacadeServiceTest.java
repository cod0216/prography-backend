package com.prography.backend.domain.attendance.service;

import com.prography.backend.domain.attendance.dto.request.AdminAttendanceRegisterRequest;
import com.prography.backend.domain.attendance.dto.request.AdminAttendanceUpdateRequest;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
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
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.domain.attendance.service<br>
 * fileName       : AdminAttendanceFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 출결 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class AdminAttendanceFacadeServiceTest {

    @Autowired
    private AdminAttendanceFacadeService adminAttendanceFacadeService;

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
    private DepositHistoryRepository depositHistoryRepository;

    @Test
    void registerAttendance_absent_appliesPenaltyAndCreatesHistory() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("user1");
        SessionEntity session = createSession(cohortMember.getCohort(), "정기 모임");

        AdminAttendanceRegisterRequest request = new AdminAttendanceRegisterRequest();
        setField(request, "sessionId", session.getId());
        setField(request, "memberId", cohortMember.getMember().getId());
        setField(request, "status", AttendanceStatus.ABSENT);
        setField(request, "lateMinutes", null);
        setField(request, "reason", "무단 결석");

        // When
        AttendanceResponse response = adminAttendanceFacadeService.registerAttendance(request);

        // Then
        assertThat(response.getPenaltyAmount()).isEqualTo(PolicyConstants.PENALTY_ABSENT);
        assertThat(cohortMemberService.getById(cohortMember.getId()).getDeposit())
                .isEqualTo(PolicyConstants.INITIAL_DEPOSIT - PolicyConstants.PENALTY_ABSENT);

        List<DepositHistoryEntity> histories = depositHistoryRepository.findByCohortMemberIdOrderByCreatedAtAsc(cohortMember.getId());
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getType()).isEqualTo(DepositType.PENALTY);
        assertThat(histories.get(0).getAmount()).isEqualTo(-PolicyConstants.PENALTY_ABSENT);
        assertThat(histories.get(0).getAttendanceId()).isEqualTo(response.getId());
    }

    @Test
    void updateAttendance_absentToExcused_refundsAndUpdatesExcuseCount() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("user2");
        SessionEntity session = createSession(cohortMember.getCohort(), "정기 모임");

        AdminAttendanceRegisterRequest registerRequest = new AdminAttendanceRegisterRequest();
        setField(registerRequest, "sessionId", session.getId());
        setField(registerRequest, "memberId", cohortMember.getMember().getId());
        setField(registerRequest, "status", AttendanceStatus.ABSENT);
        setField(registerRequest, "lateMinutes", null);
        setField(registerRequest, "reason", "무단 결석");

        AttendanceResponse registered = adminAttendanceFacadeService.registerAttendance(registerRequest);

        AdminAttendanceUpdateRequest updateRequest = new AdminAttendanceUpdateRequest();
        setField(updateRequest, "status", AttendanceStatus.EXCUSED);
        setField(updateRequest, "lateMinutes", null);
        setField(updateRequest, "reason", "병가");

        // When
        AttendanceResponse updated = adminAttendanceFacadeService.updateAttendance(registered.getId(), updateRequest);

        // Then
        assertThat(updated.getStatus()).isEqualTo(AttendanceStatus.EXCUSED);
        assertThat(updated.getPenaltyAmount()).isZero();
        assertThat(cohortMemberService.getById(cohortMember.getId()).getDeposit()).isEqualTo(PolicyConstants.INITIAL_DEPOSIT);
        assertThat(cohortMemberService.getById(cohortMember.getId()).getExcuseCount()).isEqualTo(1);

        List<DepositHistoryEntity> histories = depositHistoryRepository.findByCohortMemberIdOrderByCreatedAtAsc(cohortMember.getId());
        assertThat(histories).hasSize(2);
        assertThat(histories).anyMatch(history -> history.getType() == DepositType.REFUND
                && history.getAmount().equals(PolicyConstants.PENALTY_ABSENT));
    }

    private CohortMemberEntity createCohortMember(String loginId) {
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(PolicyConstants.CURRENT_GENERATION).name("11기").build());
        MemberEntity member = memberService.createMember(
                loginId,
                passwordEncoder.encode("password123"),
                "홍길동",
                "010-1234-5678",
                MemberRole.MEMBER
        );
        return cohortMemberService.create(member, cohort, null, null, PolicyConstants.INITIAL_DEPOSIT);
    }

    private SessionEntity createSession(CohortEntity cohort, String title) {
        return sessionRepository.save(SessionEntity.builder()
                .cohort(cohort)
                .title(title)
                .date(LocalDate.of(2026, 2, 24))
                .time(LocalTime.of(10, 0))
                .location("강남")
                .status(SessionStatus.SCHEDULED)
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
