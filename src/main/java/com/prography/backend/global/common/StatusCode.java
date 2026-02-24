package com.prography.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.prography.backend.global.common<br>
 * fileName       : StatusCode.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : API 에러 코드를 정의하는 enum 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@RequiredArgsConstructor
public enum StatusCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "로그인 아이디 또는 비밀번호가 올바르지 않습니다"),
    MEMBER_WITHDRAWN(HttpStatus.FORBIDDEN, "MEMBER_WITHDRAWN", "탈퇴한 회원입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "DUPLICATE_LOGIN_ID", "이미 사용 중인 로그인 아이디입니다"),
    MEMBER_ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "MEMBER_ALREADY_WITHDRAWN", "이미 탈퇴한 회원입니다"),
    COHORT_NOT_FOUND(HttpStatus.NOT_FOUND, "COHORT_NOT_FOUND", "기수를 찾을 수 없습니다"),
    PART_NOT_FOUND(HttpStatus.NOT_FOUND, "PART_NOT_FOUND", "파트를 찾을 수 없습니다"),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM_NOT_FOUND", "팀을 찾을 수 없습니다"),
    COHORT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "COHORT_MEMBER_NOT_FOUND", "기수 회원 정보를 찾을 수 없습니다"),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION_NOT_FOUND", "일정을 찾을 수 없습니다"),
    SESSION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "SESSION_ALREADY_CANCELLED", "이미 취소된 일정입니다"),
    SESSION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "SESSION_NOT_IN_PROGRESS", "진행 중인 일정이 아닙니다"),
    QR_NOT_FOUND(HttpStatus.NOT_FOUND, "QR_NOT_FOUND", "QR 코드를 찾을 수 없습니다"),
    QR_INVALID(HttpStatus.BAD_REQUEST, "QR_INVALID", "유효하지 않은 QR 코드입니다"),
    QR_EXPIRED(HttpStatus.BAD_REQUEST, "QR_EXPIRED", "만료된 QR 코드입니다"),
    QR_ALREADY_ACTIVE(HttpStatus.CONFLICT, "QR_ALREADY_ACTIVE", "이미 활성화된 QR 코드가 있습니다"),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ATTENDANCE_NOT_FOUND", "출결 기록을 찾을 수 없습니다"),
    ATTENDANCE_ALREADY_CHECKED(HttpStatus.CONFLICT, "ATTENDANCE_ALREADY_CHECKED", "이미 출결 체크가 완료되었습니다"),
    EXCUSE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "EXCUSE_LIMIT_EXCEEDED", "공결 횟수를 초과했습니다 (최대 3회)"),
    DEPOSIT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "DEPOSIT_INSUFFICIENT", "보증금 잔액이 부족합니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
