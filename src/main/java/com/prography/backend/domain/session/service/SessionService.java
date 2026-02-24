package com.prography.backend.domain.session.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.repository.SessionRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
 * 2026-02-24         cod0216             일정 생성/수정/취소 기능 추가<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public SessionEntity create(CohortEntity cohort, String title, LocalDate date, LocalTime time, String location) {
        SessionEntity session = SessionEntity.builder()
                .cohort(cohort)
                .title(title)
                .date(date)
                .time(time)
                .location(location)
                .status(SessionStatus.SCHEDULED)
                .build();

        return sessionRepository.save(session);
    }

    public SessionEntity getById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.SESSION_NOT_FOUND));
    }

    public List<SessionEntity> getByCohortId(Long cohortId) {
        return sessionRepository.findByCohortIdOrderByDateAscTimeAsc(cohortId);
    }

    @Transactional
    public SessionEntity update(SessionEntity session, String title, LocalDate date, LocalTime time, String location, SessionStatus status) {
        session.updateInfo(title, date, time, location);
        session.updateStatus(status);
        return session;
    }

    @Transactional
    public SessionEntity cancel(SessionEntity session) {
        session.cancel();
        return session;
    }
}
