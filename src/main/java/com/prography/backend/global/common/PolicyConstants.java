package com.prography.backend.global.common;

/**
 * packageName    : com.prography.backend.global.common<br>
 * fileName       : PolicyConstants.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 정책 상수를 모아둔 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 * 2026-02-24         cod0216             출결 정책 상수 추가<br>
 */
public final class PolicyConstants {

    private PolicyConstants() {
        throw new UnsupportedOperationException("Utility 클래스는 생성할 수 없습니다.");
    }

    public static final long INITIAL_DEPOSIT = 100_000L;
    public static final String INITIAL_DEPOSIT_DESCRIPTION = "초기 보증금";
    public static final int CURRENT_GENERATION = 11;
    public static final int EXCUSE_LIMIT = 3;
    public static final long PENALTY_ABSENT = 10_000L;
    public static final long PENALTY_LATE_PER_MINUTE = 500L;
    public static final long PENALTY_MAX = 10_000L;
}
