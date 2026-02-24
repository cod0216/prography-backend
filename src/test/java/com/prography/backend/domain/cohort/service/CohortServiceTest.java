package com.prography.backend.domain.cohort.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.cohort.service<br>
 * fileName       : CohortServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class CohortServiceTest {

    @Autowired
    private CohortService cohortService;

    @Autowired
    private CohortRepository cohortRepository;

    @Test
    void getById_returnsCohort() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder()
                .generation(11)
                .name("11기")
                .build());

        // When
        CohortEntity result = cohortService.getById(cohort.getId());

        // Then
        assertThat(result.getId()).isEqualTo(cohort.getId());
        assertThat(result.getGeneration()).isEqualTo(11);
    }

    @Test
    void getById_notFound_throwsException() {
        // Given
        Long unknownId = 999L;

        // When & Then
        assertThatThrownBy(() -> cohortService.getById(unknownId))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.COHORT_NOT_FOUND);
    }
}
