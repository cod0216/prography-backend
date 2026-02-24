package com.prography.backend.domain.attendance.controller;

import com.prography.backend.domain.attendance.dto.request.AdminAttendanceRegisterRequest;
import com.prography.backend.domain.attendance.dto.request.AdminAttendanceUpdateRequest;
import com.prography.backend.domain.attendance.dto.response.AdminMemberAttendanceDetailResponse;
import com.prography.backend.domain.attendance.dto.response.AdminSessionAttendanceSummaryResponse;
import com.prography.backend.domain.attendance.dto.response.AdminSessionAttendancesResponse;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.service.AdminAttendanceFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.controller<br>
 * fileName       : AdminAttendanceController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 출결 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/attendances")
public class AdminAttendanceController {

    private final AdminAttendanceFacadeService adminAttendanceFacadeService;

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> registerAttendance(@RequestBody @Valid AdminAttendanceRegisterRequest request) {
        return ResponseUtility.created(adminAttendanceFacadeService.registerAttendance(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
            @PathVariable Long id,
            @RequestBody @Valid AdminAttendanceUpdateRequest request
    ) {
        return ResponseUtility.success(adminAttendanceFacadeService.updateAttendance(id, request));
    }

    @GetMapping("/sessions/{sessionId}/summary")
    public ResponseEntity<ApiResponse<List<AdminSessionAttendanceSummaryResponse>>> getSessionSummary(@PathVariable Long sessionId) {
        return ResponseUtility.success(adminAttendanceFacadeService.getSessionSummary(sessionId));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse<AdminMemberAttendanceDetailResponse>> getMemberAttendanceDetail(@PathVariable Long memberId) {
        return ResponseUtility.success(adminAttendanceFacadeService.getMemberAttendanceDetail(memberId));
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<AdminSessionAttendancesResponse>> getSessionAttendances(@PathVariable Long sessionId) {
        return ResponseUtility.success(adminAttendanceFacadeService.getSessionAttendances(sessionId));
    }
}
