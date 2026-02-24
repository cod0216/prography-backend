package com.prography.backend.domain.cohort.service;

import com.prography.backend.domain.cohort.dto.response.CohortDetailResponse;
import com.prography.backend.domain.cohort.dto.response.CohortSummaryResponse;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.mapper.CohortMapper;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.service.PartService;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.domain.cohort.service<br>
 * fileName       : CohortFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 관련 복합 조회를 제공하는 파사드 서비스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CohortFacadeService {

    private final CohortService cohortService;
    private final PartService partService;
    private final TeamService teamService;
    private final CohortMapper cohortMapper;

    public List<CohortSummaryResponse> getCohortSummaries() {
        return cohortService.getCohorts().stream()
                .map(cohortMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    public CohortDetailResponse getCohortDetail(Long cohortId) {
        CohortEntity cohort = cohortService.getById(cohortId);
        List<PartEntity> parts = partService.getByCohortId(cohortId);
        List<TeamEntity> teams = teamService.getByCohortId(cohortId);
        return cohortMapper.toDetailResponse(cohort, parts, teams);
    }
}
