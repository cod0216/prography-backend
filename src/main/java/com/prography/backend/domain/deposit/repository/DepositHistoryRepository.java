package com.prography.backend.domain.deposit.repository;

import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.deposit.repository<br>
 * fileName       : DepositHistoryRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 보증금 이력 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
public interface DepositHistoryRepository extends JpaRepository<DepositHistoryEntity, Long> {
    List<DepositHistoryEntity> findByCohortMemberIdOrderByCreatedAtAsc(Long cohortMemberId);
}
