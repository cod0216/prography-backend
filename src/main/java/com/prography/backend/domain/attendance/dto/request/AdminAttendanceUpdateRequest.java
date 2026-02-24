package com.prography.backend.domain.attendance.dto.request;

import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * packageName    : com.prography.backend.domain.attendance.dto.request<br>
 * fileName       : AdminAttendanceUpdateRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 출결 수정 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
public class AdminAttendanceUpdateRequest {

    @NotNull(message = "status는 필수입니다.")
    private AttendanceStatus status;

    @Min(value = 0, message = "lateMinutes는 0 이상이어야 합니다.")
    private Integer lateMinutes;

    private String reason;
}
