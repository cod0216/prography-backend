package com.prography.backend.domain.cohort.mapper;

import com.prography.backend.domain.cohort.dto.response.CohortDetailResponse;
import com.prography.backend.domain.cohort.dto.response.CohortSummaryResponse;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.part.dto.response.PartSummaryResponse;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.mapper.PartMapper;
import com.prography.backend.domain.team.dto.response.TeamSummaryResponse;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.domain.cohort.mapper<br>
 * fileName       : CohortMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class CohortMapper {

    private final PartMapper partMapper;
    private final TeamMapper teamMapper;

    public CohortSummaryResponse toSummaryResponse(CohortEntity cohort) {
        if (cohort == null) {
            return null;
        }
        return CohortSummaryResponse.builder()
                .id(cohort.getId())
                .generation(cohort.getGeneration())
                .name(cohort.getName())
                .createdAt(cohort.getCreatedAt())
                .build();
    }

    public CohortDetailResponse toDetailResponse(CohortEntity cohort, List<PartEntity> parts, List<TeamEntity> teams) {
        if (cohort == null) {
            return null;
        }
        return CohortDetailResponse.builder()
                .id(cohort.getId())
                .generation(cohort.getGeneration())
                .name(cohort.getName())
                .parts(mapParts(parts))
                .teams(mapTeams(teams))
                .createdAt(cohort.getCreatedAt())
                .build();
    }

    private List<PartSummaryResponse> mapParts(List<PartEntity> parts) {
        if (parts == null) {
            return List.of();
        }
        return parts.stream()
                .map(partMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    private List<TeamSummaryResponse> mapTeams(List<TeamEntity> teams) {
        if (teams == null) {
            return List.of();
        }
        return teams.stream()
                .map(teamMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }
}
