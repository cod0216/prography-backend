package com.prography.backend.domain.attendance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.request<br>
 * fileName       : MemberAttendanceCheckInRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 QR 출석 체크 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
public class MemberAttendanceCheckInRequest {

    @NotBlank(message = "hashValue는 필수입니다.")
    private String hashValue;

    @NotNull(message = "memberId는 필수입니다.")
    private Long memberId;
}
