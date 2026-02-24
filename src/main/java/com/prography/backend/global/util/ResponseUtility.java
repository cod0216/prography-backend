package com.prography.backend.global.util;

import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.prography.backend.global.util<br>
 * fileName       : ResponseUtility.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 공통 응답 생성을 위한 유틸리티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
public final class ResponseUtility {
    private ResponseUtility() {
        throw new UnsupportedOperationException("Utility 클래스는 생성할 수 없습니다.");
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static ResponseEntity<ApiResponse<Void>> failure(StatusCode statusCode) {
        return ResponseEntity.status(statusCode.getStatus())
                .body(ApiResponse.error(statusCode));
    }

    public static ResponseEntity<ApiResponse<Void>> failure(StatusCode statusCode, String message) {
        return ResponseEntity.status(statusCode.getStatus())
                .body(ApiResponse.error(statusCode, message));
    }
}
