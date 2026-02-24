package com.prography.backend.domain.part.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.repository.PartRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.part.service<br>
 * fileName       : PartServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 파트 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class PartServiceTest {

    @Autowired
    private PartService partService;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Test
    void getById_returnsPart() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder()
                .generation(11)
                .name("11기")
                .build());
        PartEntity part = partRepository.save(PartEntity.builder()
                .name("SERVER")
                .cohort(cohort)
                .build());

        // When
        PartEntity result = partService.getById(part.getId());

        // Then
        assertThat(result.getId()).isEqualTo(part.getId());
        assertThat(result.getName()).isEqualTo("SERVER");
    }

    @Test
    void getById_notFound_throwsException() {
        // Given
        Long unknownId = 999L;

        // When & Then
        assertThatThrownBy(() -> partService.getById(unknownId))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.PART_NOT_FOUND);
    }
}
