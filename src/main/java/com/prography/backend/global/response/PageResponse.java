package com.prography.backend.global.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.prography.backend.global.response<br>
 * fileName       : PageResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 페이징 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
