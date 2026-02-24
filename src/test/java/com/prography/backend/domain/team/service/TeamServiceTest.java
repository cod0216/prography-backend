package com.prography.backend.domain.team.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.repository.TeamRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.team.service<br>
 * fileName       : TeamServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 팀 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Test
    void getById_returnsTeam() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder()
                .generation(11)
                .name("11기")
                .build());
        TeamEntity team = teamRepository.save(TeamEntity.builder()
                .name("Team A")
                .cohort(cohort)
                .build());

        // When
        TeamEntity result = teamService.getById(team.getId());

        // Then
        assertThat(result.getId()).isEqualTo(team.getId());
        assertThat(result.getName()).isEqualTo("Team A");
    }

    @Test
    void getById_notFound_throwsException() {
        // Given
        Long unknownId = 999L;

        // When & Then
        assertThatThrownBy(() -> teamService.getById(unknownId))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.TEAM_NOT_FOUND);
    }

    @Test
    void getByCohortId_returnsTeams() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder()
                .generation(11)
                .name("11기")
                .build());
        TeamEntity team1 = teamRepository.save(TeamEntity.builder()
                .name("Team A")
                .cohort(cohort)
                .build());
        TeamEntity team2 = teamRepository.save(TeamEntity.builder()
                .name("Team B")
                .cohort(cohort)
                .build());

        // When
        var teams = teamService.getByCohortId(cohort.getId());

        // Then
        assertThat(teams).hasSize(2);
        assertThat(teams).extracting("id")
                .containsExactly(team1.getId(), team2.getId());
    }
}
