package com.prography.backend.domain.qrcode.repository;

import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.qrcode.repository<br>
 * fileName       : QrCodeRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : QR 코드 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             활성 QR 조회 메서드 추가<br>
 */
public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Long> {
    Optional<QrCodeEntity> findByHashValue(String hashValue);
    Optional<QrCodeEntity> findFirstBySessionIdAndExpiresAtAfter(Long sessionId, LocalDateTime now);
    List<QrCodeEntity> findBySessionIdInAndExpiresAtAfter(Collection<Long> sessionIds, LocalDateTime now);
}
