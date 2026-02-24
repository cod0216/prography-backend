package com.prography.backend.domain.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.response<br>
 * fileName       : AdminSessionAttendancesResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정별 출결 목록 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class AdminSessionAttendancesResponse {
    private Long sessionId;
    private String sessionTitle;
    private List<AttendanceResponse> attendances;
}
