package com.prography.backend.domain.auth.controller;

import com.prography.backend.domain.auth.dto.request.LoginRequest;
import com.prography.backend.domain.auth.service.AuthFacadeService;
import com.prography.backend.domain.member.dto.response.MemberResponse;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.prography.backend.domain.auth.controller<br>
 * fileName       : AuthController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 로그인 요청을 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthFacadeService authFacadeService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseUtility.success(authFacadeService.login(request));
    }
}
