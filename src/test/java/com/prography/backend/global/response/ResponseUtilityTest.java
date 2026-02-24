package com.prography.backend.global.response;

import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.util.ResponseUtility;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.global.response<br>
 * fileName       : ResponseUtilityTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : ResponseUtility에 대한 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
class ResponseUtilityTest {

    @Test
    void success_returnsOkResponse() {
        // Given
        String data = "hello";

        // When
        ResponseEntity<ApiResponse<String>> response = ResponseUtility.success(data);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).isEqualTo(data);
    }

    @Test
    void failure_returnsErrorResponse() {
        // Given
        StatusCode statusCode = StatusCode.MEMBER_NOT_FOUND;

        // When
        ResponseEntity<ApiResponse<Void>> response = ResponseUtility.failure(statusCode);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(statusCode.getStatus());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getError().getCode()).isEqualTo(statusCode.getCode());
    }
}
