package com.prography.backend.domain.qrcode.controller;

import com.prography.backend.domain.qrcode.dto.response.QrCodeResponse;
import com.prography.backend.domain.qrcode.service.AdminQrCodeFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.prography.backend.domain.qrcode.controller<br>
 * fileName       : AdminQrCodeController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 QR 코드 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminQrCodeController {

    private final AdminQrCodeFacadeService adminQrCodeFacadeService;

    @PostMapping("/sessions/{sessionId}/qrcodes")
    public ResponseEntity<ApiResponse<QrCodeResponse>> createQrCode(@PathVariable Long sessionId) {
        return ResponseUtility.created(adminQrCodeFacadeService.createQrCode(sessionId));
    }

    @PutMapping("/qrcodes/{qrCodeId}")
    public ResponseEntity<ApiResponse<QrCodeResponse>> renewQrCode(@PathVariable Long qrCodeId) {
        return ResponseUtility.success(adminQrCodeFacadeService.renewQrCode(qrCodeId));
    }
}
