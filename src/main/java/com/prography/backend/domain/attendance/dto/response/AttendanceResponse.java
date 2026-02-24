package com.prography.backend.domain.attendance.dto.response;

import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.response<br>
 * fileName       : AttendanceResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 출결 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class AttendanceResponse {
    private Long id;
    private Long sessionId;
    private Long memberId;
    private AttendanceStatus status;
    private Integer lateMinutes;
    private Long penaltyAmount;
    private String reason;
    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
