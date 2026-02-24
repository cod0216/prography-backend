package com.prography.backend.domain.session.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.session.dto.response.MemberSessionResponse;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.PolicyConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.domain.session.service<br>
 * fileName       : MemberSessionFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 일정 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class MemberSessionFacadeServiceTest {

    @Autowired
    private MemberSessionFacadeService memberSessionFacadeService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void getSessions_excludesCancelledAndOtherCohort() {
        // Given
        CohortEntity current = createCohort(PolicyConstants.CURRENT_GENERATION);
        CohortEntity old = createCohort(10);

        SessionEntity included = createSession(current, SessionStatus.SCHEDULED, LocalDate.of(2026, 2, 25), LocalTime.of(10, 0));
        createSession(current, SessionStatus.CANCELLED, LocalDate.of(2026, 2, 26), LocalTime.of(9, 0));
        createSession(old, SessionStatus.SCHEDULED, LocalDate.of(2026, 2, 27), LocalTime.of(8, 0));

        // When
        List<MemberSessionResponse> responses = memberSessionFacadeService.getSessions();

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(included.getId());
        assertThat(responses.get(0).getStatus()).isEqualTo(SessionStatus.SCHEDULED);
    }

    private CohortEntity createCohort(int generation) {
        return cohortRepository.save(CohortEntity.builder()
                .generation(generation)
                .name(generation + "기")
                .build());
    }

    private SessionEntity createSession(CohortEntity cohort, SessionStatus status, LocalDate date, LocalTime time) {
        return sessionRepository.save(SessionEntity.builder()
                .cohort(cohort)
                .title("정기 모임")
                .date(date)
                .time(time)
                .location("강남")
                .status(status)
                .build());
    }
}
