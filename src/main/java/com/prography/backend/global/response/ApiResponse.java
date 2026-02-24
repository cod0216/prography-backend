package com.prography.backend.global.response;

import com.prography.backend.global.common.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.global.response<br>
 * fileName       : ApiResponse.java<br>
 * author         : codex <br>
 * date           : 2026-02-24<br>
 * description    : 공통 응답 포맷을 정의하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(StatusCode statusCode) {
        return new ApiResponse<>(false, null, ErrorResponse.from(statusCode));
    }

    public static <T> ApiResponse<T> error(StatusCode statusCode, String message) {
        return new ApiResponse<>(false, null, ErrorResponse.from(statusCode, message));
    }
}
