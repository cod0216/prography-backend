package com.prography.backend.global.response;

import com.prography.backend.global.common.StatusCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.global.response<br>
 * fileName       : ApiResponseTest.java<br>
 * author         : codex <br>
 * date           : 2026-02-24<br>
 * description    : ApiResponse에 대한 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
class ApiResponseTest {

    @Test
    void successResponse_setsDataAndSuccess() {
        // Given
        String data = "ok";

        // When
        ApiResponse<String> response = ApiResponse.success(data);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getError()).isNull();
    }

    @Test
    void errorResponse_setsErrorAndSuccessFalse() {
        // Given
        StatusCode statusCode = StatusCode.INVALID_INPUT;

        // When
        ApiResponse<Void> response = ApiResponse.error(statusCode);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getData()).isNull();
        assertThat(response.getError()).isNotNull();
        assertThat(response.getError().getCode()).isEqualTo(statusCode.getCode());
        assertThat(response.getError().getMessage()).isEqualTo(statusCode.getMessage());
    }
}
