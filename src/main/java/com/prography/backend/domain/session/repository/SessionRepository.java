package com.prography.backend.domain.session.repository;

import com.prography.backend.domain.session.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.prography.backend.domain.session.repository<br>
 * fileName       : SessionRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 일정 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
}
