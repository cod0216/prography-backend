package com.prography.backend.domain.qrcode.service;

import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.repository.QrCodeRepository;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * packageName    : com.prography.backend.domain.qrcode.service<br>
 * fileName       : QrCodeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : QR 코드 조회 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             QR 생성 및 활성 조회 기능 추가<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    @Transactional(readOnly = true)
    public QrCodeEntity getByHashValue(String hashValue) {
        return qrCodeRepository.findByHashValue(hashValue)
                .orElseThrow(() -> new CustomException(StatusCode.QR_INVALID));
    }

    @Transactional(readOnly = true)
    public QrCodeEntity getById(Long id) {
        return qrCodeRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.QR_NOT_FOUND));
    }

    public QrCodeEntity create(SessionEntity session, LocalDateTime expiresAt) {
        QrCodeEntity qrCode = QrCodeEntity.builder()
                .session(session)
                .hashValue(UUID.randomUUID().toString())
                .expiresAt(expiresAt)
                .build();
        return qrCodeRepository.save(qrCode);
    }

    public Optional<QrCodeEntity> findActiveBySessionId(Long sessionId, LocalDateTime now) {
        return qrCodeRepository.findFirstBySessionIdAndExpiresAtAfter(sessionId, now);
    }

    public List<QrCodeEntity> findActiveBySessionIds(Collection<Long> sessionIds, LocalDateTime now) {
        if (sessionIds == null || sessionIds.isEmpty()) {
            return List.of();
        }
        return qrCodeRepository.findBySessionIdInAndExpiresAtAfter(sessionIds, now);
    }

    public void expire(QrCodeEntity qrCode, LocalDateTime now) {
        qrCode.updateExpiresAt(now);
    }
}
