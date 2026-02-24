package com.prography.backend.domain.cohort.service;

import com.prography.backend.domain.cohort.dto.response.CohortDetailResponse;
import com.prography.backend.domain.cohort.dto.response.CohortSummaryResponse;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.repository.PartRepository;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.domain.cohort.service<br>
 * fileName       : CohortFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class CohortFacadeServiceTest {

    @Autowired
    private CohortFacadeService cohortFacadeService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void getCohortSummaries_returnsAllCohorts() {
        // Given
        cohortRepository.save(CohortEntity.builder().generation(10).name("10기").build());
        cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());

        // When
        List<CohortSummaryResponse> result = cohortFacadeService.getCohortSummaries();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("generation")
                .containsExactly(10, 11);
    }

    @Test
    void getCohortDetail_returnsPartsAndTeams() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        CohortEntity otherCohort = cohortRepository.save(CohortEntity.builder().generation(10).name("10기").build());

        partRepository.save(PartEntity.builder().name("SERVER").cohort(cohort).build());
        partRepository.save(PartEntity.builder().name("WEB").cohort(cohort).build());
        partRepository.save(PartEntity.builder().name("iOS").cohort(otherCohort).build());

        teamRepository.save(TeamEntity.builder().name("Team A").cohort(cohort).build());
        teamRepository.save(TeamEntity.builder().name("Team B").cohort(cohort).build());
        teamRepository.save(TeamEntity.builder().name("Team X").cohort(otherCohort).build());

        // When
        CohortDetailResponse detail = cohortFacadeService.getCohortDetail(cohort.getId());

        // Then
        assertThat(detail.getId()).isEqualTo(cohort.getId());
        assertThat(detail.getParts()).extracting("name")
                .containsExactly("SERVER", "WEB");
        assertThat(detail.getTeams()).extracting("name")
                .containsExactly("Team A", "Team B");
    }
}
