package com.prography.backend.domain.qrcode.service;

import com.prography.backend.domain.qrcode.dto.response.QrCodeResponse;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.mapper.QrCodeMapper;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.service.SessionService;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * packageName    : com.prography.backend.domain.qrcode.service<br>
 * fileName       : AdminQrCodeFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 QR 코드 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdminQrCodeFacadeService {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final SessionService sessionService;
    private final QrCodeService qrCodeService;
    private final QrCodeMapper qrCodeMapper;

    public QrCodeResponse createQrCode(Long sessionId) {
        SessionEntity session = sessionService.getById(sessionId);

        if (qrCodeService.findActiveBySessionId(sessionId, now()).isPresent()) {
            throw new CustomException(StatusCode.QR_ALREADY_ACTIVE);
        }

        QrCodeEntity created = qrCodeService.create(session, nextExpiresAt());
        return qrCodeMapper.toResponse(created);
    }

    public QrCodeResponse renewQrCode(Long qrCodeId) {
        QrCodeEntity existing = qrCodeService.getById(qrCodeId);

        LocalDateTime now = now();
        qrCodeService.expire(existing, now);

        QrCodeEntity created = qrCodeService.create(existing.getSession(), now.plusHours(PolicyConstants.QR_EXPIRE_HOURS));
        return qrCodeMapper.toResponse(created);
    }

    private LocalDateTime nextExpiresAt() {
        return now().plusHours(PolicyConstants.QR_EXPIRE_HOURS);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(KOREA_ZONE);
    }
}
