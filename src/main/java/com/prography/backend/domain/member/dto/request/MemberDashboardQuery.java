package com.prography.backend.domain.member.dto.request;

import com.prography.backend.domain.member.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.member.dto.request<br>
 * fileName       : MemberDashboardQuery.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 회원 대시보드 조회 조건 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
@Builder
public class MemberDashboardQuery {
    private int page;
    private int size;
    private String searchType;
    private String searchValue;
    private Integer generation;
    private String partName;
    private String teamName;
    private MemberStatus status;
}
