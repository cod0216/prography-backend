package com.prography.backend.domain.team.service;

import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.repository.TeamRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.team.service<br>
 * fileName       : TeamService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 팀 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamEntity getById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.TEAM_NOT_FOUND));
    }

    public List<TeamEntity> getByCohortId(Long cohortId) {
        return teamRepository.findByCohortIdOrderByNameAsc(cohortId);
    }
}
