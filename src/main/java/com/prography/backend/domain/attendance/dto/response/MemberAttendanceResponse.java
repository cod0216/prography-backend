package com.prography.backend.domain.attendance.dto.response;

import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.response<br>
 * fileName       : MemberAttendanceResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 출결 기록 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class MemberAttendanceResponse {
    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private AttendanceStatus status;
    private Integer lateMinutes;
    private Long penaltyAmount;
    private String reason;
    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
}
