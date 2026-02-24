package com.prography.backend.global.error;

import com.prography.backend.global.common.StatusCode;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.global.error<br>
 * fileName       : CustomException.java<br>
 * author         : codex <br>
 * date           : 2026-02-24<br>
 * description    : 전역에서 사용하는 커스텀 예외 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
@Getter
public class CustomException extends RuntimeException {
    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }

    public CustomException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
