package com.prography.backend.domain.qrcode.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.qrcode.dto.response.QrCodeResponse;
import com.prography.backend.domain.qrcode.entity.QrCodeEntity;
import com.prography.backend.domain.qrcode.repository.QrCodeRepository;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.qrcode.service<br>
 * fileName       : AdminQrCodeFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 QR 코드 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class AdminQrCodeFacadeServiceTest {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @Autowired
    private AdminQrCodeFacadeService adminQrCodeFacadeService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Test
    void createQrCode_activeExists_throwsConflict() {
        // Given
        SessionEntity session = createSession();
        qrCodeRepository.save(QrCodeEntity.builder()
                .session(session)
                .hashValue("active-qr")
                .expiresAt(now().plusHours(1))
                .build());

        // When & Then
        assertThatThrownBy(() -> adminQrCodeFacadeService.createQrCode(session.getId()))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.QR_ALREADY_ACTIVE);
    }

    @Test
    void renewQrCode_expiresOldAndCreatesNew() {
        // Given
        SessionEntity session = createSession();
        QrCodeEntity oldQr = qrCodeRepository.save(QrCodeEntity.builder()
                .session(session)
                .hashValue("old-qr")
                .expiresAt(now().plusHours(2))
                .build());

        // When
        QrCodeResponse renewed = adminQrCodeFacadeService.renewQrCode(oldQr.getId());

        // Then
        QrCodeEntity expiredOld = qrCodeRepository.findById(oldQr.getId()).orElseThrow();
        QrCodeEntity newQr = qrCodeRepository.findById(renewed.getId()).orElseThrow();

        assertThat(newQr.getId()).isNotEqualTo(oldQr.getId());
        assertThat(expiredOld.getExpiresAt()).isBeforeOrEqualTo(now());
        assertThat(newQr.getExpiresAt()).isAfter(expiredOld.getExpiresAt());
        assertThat(newQr.getSession().getId()).isEqualTo(session.getId());
    }

    private SessionEntity createSession() {
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder()
                .generation(PolicyConstants.CURRENT_GENERATION)
                .name("11기")
                .build());

        return sessionRepository.save(SessionEntity.builder()
                .cohort(cohort)
                .title("정기 모임")
                .date(LocalDate.of(2026, 2, 25))
                .time(LocalTime.of(10, 0))
                .location("강남")
                .status(SessionStatus.SCHEDULED)
                .build());
    }

    private LocalDateTime now() {
        return LocalDateTime.now(KOREA_ZONE);
    }
}
