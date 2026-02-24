package com.prography.backend.domain.deposit.dto.response;

import com.prography.backend.domain.deposit.entity.DepositType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.deposit.dto.response<br>
 * fileName       : DepositHistoryResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 보증금 이력 응답 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@Builder
public class DepositHistoryResponse {
    private Long id;
    private Long cohortMemberId;
    private DepositType type;
    private Long amount;
    private Long balanceAfter;
    private Long attendanceId;
    private String description;
    private LocalDateTime createdAt;
}
