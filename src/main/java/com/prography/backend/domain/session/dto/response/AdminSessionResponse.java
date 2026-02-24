package com.prography.backend.domain.session.dto.response;

import com.prography.backend.domain.session.entity.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * packageName    : com.prography.backend.domain.session.dto.response<br>
 * fileName       : AdminSessionResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class AdminSessionResponse {
    private Long id;
    private Long cohortId;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private SessionStatus status;
    private SessionAttendanceSummaryResponse attendanceSummary;
    private boolean qrActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
