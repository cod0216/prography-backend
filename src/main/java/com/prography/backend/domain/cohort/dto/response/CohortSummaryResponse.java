package com.prography.backend.domain.cohort.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.cohort.dto.response<br>
 * fileName       : CohortSummaryResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 요약 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class CohortSummaryResponse {
    private Long id;
    private Integer generation;
    private String name;
    private LocalDateTime createdAt;
}
