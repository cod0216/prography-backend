package com.prography.backend.domain.deposit.mapper;

import com.prography.backend.domain.deposit.dto.response.DepositHistoryResponse;
import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.deposit.mapper<br>
 * fileName       : DepositHistoryMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 보증금 이력 응답 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class DepositHistoryMapper {

    public DepositHistoryResponse toResponse(DepositHistoryEntity history) {
        if (history == null) {
            return null;
        }

        return DepositHistoryResponse.builder()
                .id(history.getId())
                .cohortMemberId(history.getCohortMember().getId())
                .type(history.getType())
                .amount(history.getAmount())
                .balanceAfter(history.getBalanceAfter())
                .attendanceId(history.getAttendanceId())
                .description(history.getDescription())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
