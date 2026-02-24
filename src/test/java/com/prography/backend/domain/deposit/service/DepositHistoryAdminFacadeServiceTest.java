package com.prography.backend.domain.deposit.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.dto.response.DepositHistoryResponse;
import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import com.prography.backend.domain.deposit.entity.DepositType;
import com.prography.backend.domain.deposit.repository.DepositHistoryRepository;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.deposit.service<br>
 * fileName       : DepositHistoryAdminFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 보증금 이력 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class DepositHistoryAdminFacadeServiceTest {

    @Autowired
    private DepositHistoryAdminFacadeService depositHistoryAdminFacadeService;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired
    private CohortMemberService cohortMemberService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getDepositHistories_returnsOrderedHistoryList() {
        // Given
        CohortMemberEntity cohortMember = createCohortMember("member-1");
        DepositHistoryEntity third = saveHistory(cohortMember, DepositType.REFUND, 10_000L, 100_000L, 1L, "환급");
        DepositHistoryEntity first = saveHistory(cohortMember, DepositType.INITIAL, 100_000L, 100_000L, null, "초기 보증금");
        DepositHistoryEntity second = saveHistory(cohortMember, DepositType.PENALTY, -10_000L, 90_000L, 1L, "패널티");

        updateCreatedAt(first.getId(), LocalDateTime.of(2026, 2, 24, 9, 0, 0));
        updateCreatedAt(second.getId(), LocalDateTime.of(2026, 2, 24, 10, 0, 0));
        updateCreatedAt(third.getId(), LocalDateTime.of(2026, 2, 24, 11, 0, 0));

        // When
        List<DepositHistoryResponse> histories = depositHistoryAdminFacadeService.getDepositHistories(cohortMember.getId());

        // Then
        assertThat(histories).hasSize(3);
        assertThat(histories).extracting(DepositHistoryResponse::getType)
                .containsExactly(DepositType.INITIAL, DepositType.PENALTY, DepositType.REFUND);
        assertThat(histories).extracting(DepositHistoryResponse::getAmount)
                .containsExactly(100_000L, -10_000L, 10_000L);
        assertThat(histories).allSatisfy(history ->
                assertThat(history.getCohortMemberId()).isEqualTo(cohortMember.getId()));
    }

    @Test
    void getDepositHistories_whenCohortMemberMissing_throwsCohortMemberNotFound() {
        // Given
        Long missingCohortMemberId = 9999L;

        // When & Then
        assertThatThrownBy(() -> depositHistoryAdminFacadeService.getDepositHistories(missingCohortMemberId))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.COHORT_MEMBER_NOT_FOUND);
    }

    private CohortMemberEntity createCohortMember(String loginId) {
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        MemberEntity member = memberService.createMember(
                loginId,
                passwordEncoder.encode("password123"),
                "홍길동",
                "010-1234-5678",
                MemberRole.MEMBER
        );

        return cohortMemberService.create(member, cohort, null, null, 100_000L);
    }

    private DepositHistoryEntity saveHistory(CohortMemberEntity cohortMember, DepositType type, Long amount, Long balanceAfter,
                                             Long attendanceId, String description) {
        return depositHistoryRepository.save(DepositHistoryEntity.builder()
                .cohortMember(cohortMember)
                .type(type)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .attendanceId(attendanceId)
                .description(description)
                .build());
    }

    private void updateCreatedAt(Long id, LocalDateTime createdAt) {
        jdbcTemplate.update(
                "update tb_deposit_history set created_at = ? where id = ?",
                Timestamp.valueOf(createdAt),
                id
        );
    }
}
