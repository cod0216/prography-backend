package com.prography.backend.domain.session.controller;

import com.prography.backend.domain.session.dto.response.MemberSessionResponse;
import com.prography.backend.domain.session.service.MemberSessionFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.session.controller<br>
 * fileName       : SessionController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 일정 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final MemberSessionFacadeService memberSessionFacadeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberSessionResponse>>> getSessions() {
        return ResponseUtility.success(memberSessionFacadeService.getSessions());
    }
}
