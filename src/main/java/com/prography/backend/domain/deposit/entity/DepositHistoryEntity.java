package com.prography.backend.domain.deposit.entity;

import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.prography.backend.domain.deposit.entity<br>
 * fileName       : DepositHistoryEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 보증금 이력 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_deposit_history")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositHistoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cohort_member_id", nullable = false)
    private CohortMemberEntity cohortMember;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DepositType type;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;

    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column
    private String description;
}
