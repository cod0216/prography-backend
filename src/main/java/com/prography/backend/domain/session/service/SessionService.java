package com.prography.backend.domain.session.service;

import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.prography.backend.domain.session.service<br>
 * fileName       : SessionService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 일정 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionEntity getById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.SESSION_NOT_FOUND));
    }
}
