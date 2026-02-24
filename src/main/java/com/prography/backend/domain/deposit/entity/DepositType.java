package com.prography.backend.domain.deposit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.prography.backend.domain.deposit.entity<br>
 * fileName       : DepositType.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 보증금 이력 타입을 정의하는 enum 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Getter
@RequiredArgsConstructor
public enum DepositType {
    INITIAL,
    PENALTY,
    REFUND
}
