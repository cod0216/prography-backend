package com.prography.backend.domain.cohort.dto.response;

import com.prography.backend.domain.part.dto.response.PartSummaryResponse;
import com.prography.backend.domain.team.dto.response.TeamSummaryResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.prography.backend.domain.cohort.dto.response<br>
 * fileName       : CohortDetailResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 상세 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class CohortDetailResponse {
    private Long id;
    private Integer generation;
    private String name;
    private List<PartSummaryResponse> parts;
    private List<TeamSummaryResponse> teams;
    private LocalDateTime createdAt;
}
