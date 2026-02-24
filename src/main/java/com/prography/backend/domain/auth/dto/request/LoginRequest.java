package com.prography.backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.auth.dto.request<br>
 * fileName       : LoginRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 로그인 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
public class LoginRequest {

    @NotBlank(message = "loginId는 필수입니다.")
    private String loginId;

    @NotBlank(message = "password는 필수입니다.")
    private String password;
}
