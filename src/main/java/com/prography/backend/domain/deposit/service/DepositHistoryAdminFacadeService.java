package com.prography.backend.domain.deposit.service;

import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.dto.response.DepositHistoryResponse;
import com.prography.backend.domain.deposit.mapper.DepositHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.deposit.service<br>
 * fileName       : DepositHistoryAdminFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 보증금 이력 조회를 처리하는 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepositHistoryAdminFacadeService {

    private final CohortMemberService cohortMemberService;
    private final DepositHistoryService depositHistoryService;
    private final DepositHistoryMapper depositHistoryMapper;

    public List<DepositHistoryResponse> getDepositHistories(Long cohortMemberId) {
        cohortMemberService.getById(cohortMemberId);

        return depositHistoryService.getByCohortMemberIdOrderByCreatedAtAsc(cohortMemberId).stream()
                .map(depositHistoryMapper::toResponse)
                .toList();
    }
}
