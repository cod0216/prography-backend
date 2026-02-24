package com.prography.backend.domain.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.response<br>
 * fileName       : AdminMemberAttendanceDetailResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 회원 출결 상세 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class AdminMemberAttendanceDetailResponse {
    private Long memberId;
    private String memberName;
    private Integer generation;
    private String partName;
    private String teamName;
    private Long deposit;
    private Integer excuseCount;
    private List<AttendanceResponse> attendances;
}
