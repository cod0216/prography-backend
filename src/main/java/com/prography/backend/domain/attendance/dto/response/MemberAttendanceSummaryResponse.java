package com.prography.backend.domain.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.response<br>
 * fileName       : MemberAttendanceSummaryResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 출결 요약 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class MemberAttendanceSummaryResponse {
    private Long memberId;
    private int present;
    private int absent;
    private int late;
    private int excused;
    private Long totalPenalty;
    private Long deposit;
}
