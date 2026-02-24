package com.prography.backend.domain.qrcode.service;

import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.repository.QrCodeRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    public QrCodeEntity getByHashValue(String hashValue) {
        return qrCodeRepository.findByHashValue(hashValue)
                .orElseThrow(() -> new CustomException(StatusCode.QR_INVALID));
    }
}
