package com.prography.backend.domain.qrcode.mapper;

import com.prography.backend.domain.qrcode.dto.response.QrCodeResponse;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.qrcode.mapper<br>
 * fileName       : QrCodeMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : QR 코드 응답 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class QrCodeMapper {

    public QrCodeResponse toResponse(QrCodeEntity qrCode) {
        if (qrCode == null) {
            return null;
        }

        return QrCodeResponse.builder()
                .id(qrCode.getId())
                .sessionId(qrCode.getSession().getId())
                .hashValue(qrCode.getHashValue())
                .createdAt(qrCode.getCreatedAt())
                .expiresAt(qrCode.getExpiresAt())
                .build();
    }
}
