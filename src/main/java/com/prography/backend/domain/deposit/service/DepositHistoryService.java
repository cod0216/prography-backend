package com.prography.backend.domain.deposit.service;

import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import com.prography.backend.domain.deposit.entity.DepositType;
import com.prography.backend.domain.deposit.repository.DepositHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.deposit.service<br>
 * fileName       : DepositHistoryService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 보증금 이력 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 * 2026-02-24         cod0216             패널티/환급 이력 생성 추가<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DepositHistoryService {

    private final DepositHistoryRepository depositHistoryRepository;

    @Transactional(readOnly = true)
    public List<DepositHistoryEntity> getByCohortMemberIdOrderByCreatedAtAsc(Long cohortMemberId) {
        return depositHistoryRepository.findByCohortMemberIdOrderByCreatedAtAsc(cohortMemberId);
    }

    public DepositHistoryEntity createInitial(CohortMemberEntity cohortMember, Long amount, String description) {
        DepositHistoryEntity history = DepositHistoryEntity.builder()
                .cohortMember(cohortMember)
                .type(DepositType.INITIAL)
                .amount(amount)
                .balanceAfter(cohortMember.getDeposit())
                .attendanceId(null)
                .description(description)
                .build();

        return depositHistoryRepository.save(history);
    }

    public DepositHistoryEntity createPenalty(CohortMemberEntity cohortMember, Long penaltyAmount, Long attendanceId, String description) {
        DepositHistoryEntity history = DepositHistoryEntity.builder()
                .cohortMember(cohortMember)
                .type(DepositType.PENALTY)
                .amount(-penaltyAmount)
                .balanceAfter(cohortMember.getDeposit())
                .attendanceId(attendanceId)
                .description(description)
                .build();

        return depositHistoryRepository.save(history);
    }

    public DepositHistoryEntity createRefund(CohortMemberEntity cohortMember, Long refundAmount, Long attendanceId, String description) {
        DepositHistoryEntity history = DepositHistoryEntity.builder()
                .cohortMember(cohortMember)
                .type(DepositType.REFUND)
                .amount(refundAmount)
                .balanceAfter(cohortMember.getDeposit())
                .attendanceId(attendanceId)
                .description(description)
                .build();

        return depositHistoryRepository.save(history);
    }
}
