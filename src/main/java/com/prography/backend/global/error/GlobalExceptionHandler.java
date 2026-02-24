package com.prography.backend.global.error;

import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.global.error<br>
 * fileName       : GlobalExceptionHandler.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 전역 예외를 처리하는 핸들러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         codex              최초생성<br>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception) {
        return ResponseUtility.failure(exception.getStatusCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        return ResponseUtility.failure(StatusCode.INVALID_INPUT, message.isBlank() ? StatusCode.INVALID_INPUT.getMessage() : message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return ResponseUtility.failure(StatusCode.INVALID_INPUT);
        }
        return ResponseUtility.failure(StatusCode.INVALID_INPUT, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        return ResponseUtility.failure(StatusCode.INTERNAL_ERROR);
    }

    private String formatFieldError(FieldError error) {
        String defaultMessage = error.getDefaultMessage();
        if (defaultMessage == null || defaultMessage.isBlank()) {
            return error.getField();
        }
        return error.getField() + ": " + defaultMessage;
    }
}
