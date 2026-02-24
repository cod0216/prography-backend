package com.prography.backend.domain.session.controller;

import com.prography.backend.domain.session.dto.request.AdminSessionCreateRequest;
import com.prography.backend.domain.session.dto.request.AdminSessionUpdateRequest;
import com.prography.backend.domain.session.dto.response.AdminSessionResponse;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.service.AdminSessionFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.prography.backend.domain.session.controller<br>
 * fileName       : AdminSessionController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/sessions")
public class AdminSessionController {

    private final AdminSessionFacadeService adminSessionFacadeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminSessionResponse>>> getSessions(
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) SessionStatus status
    ) {
        return ResponseUtility.success(adminSessionFacadeService.getSessions(dateFrom, dateTo, status));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminSessionResponse>> createSession(@RequestBody @Valid AdminSessionCreateRequest request) {
        return ResponseUtility.created(adminSessionFacadeService.createSession(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminSessionResponse>> updateSession(
            @PathVariable Long id,
            @RequestBody AdminSessionUpdateRequest request
    ) {
        return ResponseUtility.success(adminSessionFacadeService.updateSession(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminSessionResponse>> cancelSession(@PathVariable Long id) {
        return ResponseUtility.success(adminSessionFacadeService.cancelSession(id));
    }
}
