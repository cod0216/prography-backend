package com.prography.backend.domain.member.dto.request;

import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.member.dto.request<br>
 * fileName       : AdminMemberUpdateRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 회원 수정 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
public class AdminMemberUpdateRequest {
    private String name;
    private String phone;
    private Long cohortId;
    private Long partId;
    private Long teamId;
}
