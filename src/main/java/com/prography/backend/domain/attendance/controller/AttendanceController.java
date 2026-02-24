package com.prography.backend.domain.attendance.controller;

import com.prography.backend.domain.attendance.dto.request.MemberAttendanceCheckInRequest;
import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceResponse;
import com.prography.backend.domain.attendance.service.MemberAttendanceFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.controller<br>
 * fileName       : AttendanceController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 출결 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
public class AttendanceController {

    private final MemberAttendanceFacadeService memberAttendanceFacadeService;

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(@RequestBody @Valid MemberAttendanceCheckInRequest request) {
        return ResponseUtility.created(memberAttendanceFacadeService.checkIn(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberAttendanceResponse>>> getAttendances(@RequestParam Long memberId) {
        return ResponseUtility.success(memberAttendanceFacadeService.getAttendances(memberId));
    }
}
