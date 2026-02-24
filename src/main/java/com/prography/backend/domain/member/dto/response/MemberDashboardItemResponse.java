package com.prography.backend.domain.member.dto.response;

import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.member.dto.response<br>
 * fileName       : MemberDashboardItemResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 회원 대시보드 항목 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
@Builder
public class MemberDashboardItemResponse {
    private Long id;
    private String loginId;
    private String name;
    private String phone;
    private MemberStatus status;
    private MemberRole role;
    private Integer generation;
    private String partName;
    private String teamName;
    private Long deposit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
