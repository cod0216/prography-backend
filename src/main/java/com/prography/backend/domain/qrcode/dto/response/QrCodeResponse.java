package com.prography.backend.domain.qrcode.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.qrcode.dto.response<br>
 * fileName       : QrCodeResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : QR 코드 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class QrCodeResponse {
    private Long id;
    private Long sessionId;
    private String hashValue;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
