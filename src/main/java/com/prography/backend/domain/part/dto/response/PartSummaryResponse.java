package com.prography.backend.domain.part.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.part.dto.response<br>
 * fileName       : PartSummaryResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 파트 요약 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class PartSummaryResponse {
    private Long id;
    private String name;
}
