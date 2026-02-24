package com.prography.backend.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.member.dto.request<br>
 * fileName       : AdminMemberCreateRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 회원 생성 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
public class AdminMemberCreateRequest {

    @NotBlank(message = "loginId는 필수입니다.")
    private String loginId;

    @NotBlank(message = "password는 필수입니다.")
    private String password;

    @NotBlank(message = "name은 필수입니다.")
    private String name;

    @NotBlank(message = "phone은 필수입니다.")
    private String phone;

    @NotNull(message = "cohortId는 필수입니다.")
    private Long cohortId;

    private Long partId;
    private Long teamId;
}
