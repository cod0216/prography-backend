package com.prography.backend.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.prography.backend.domain.member.entity<br>
 * fileName       : MemberStatus.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 상태를 정의하는 enum 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE,
    INACTIVE,
    WITHDRAWN
}
