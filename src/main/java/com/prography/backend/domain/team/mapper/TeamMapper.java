package com.prography.backend.domain.team.mapper;

import com.prography.backend.domain.team.dto.response.TeamSummaryResponse;
import com.prography.backend.domain.team.entity.TeamEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.team.mapper<br>
 * fileName       : TeamMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 팀 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class TeamMapper {

    public TeamSummaryResponse toSummaryResponse(TeamEntity team) {
        if (team == null) {
            return null;
        }
        return TeamSummaryResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }
}
