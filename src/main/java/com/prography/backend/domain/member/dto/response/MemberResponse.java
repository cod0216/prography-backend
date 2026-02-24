package com.prography.backend.domain.member.dto.response;

import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.member.dto.response<br>
 * fileName       : MemberResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String loginId;
    private String name;
    private String phone;
    private MemberStatus status;
    private MemberRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
