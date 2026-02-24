package com.prography.backend.global.response;

import com.prography.backend.global.common.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.global.response<br>
 * fileName       : ErrorResponse.java<br>
 * author         : codex <br>
 * date           : 2026-02-24<br>
 * description    : 실패 응답의 error 데이터를 구성하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    public static ErrorResponse from(StatusCode statusCode) {
        return new ErrorResponse(statusCode.getCode(), statusCode.getMessage());
    }

    public static ErrorResponse from(StatusCode statusCode, String message) {
        return new ErrorResponse(statusCode.getCode(), message);
    }
}
