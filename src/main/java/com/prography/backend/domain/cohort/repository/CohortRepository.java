package com.prography.backend.domain.cohort.repository;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.cohort.repository<br>
 * fileName       : CohortRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
public interface CohortRepository extends JpaRepository<CohortEntity, Long> {
    Optional<CohortEntity> findByGeneration(Integer generation);
}
