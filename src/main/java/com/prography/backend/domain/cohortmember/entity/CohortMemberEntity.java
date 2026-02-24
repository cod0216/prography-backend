package com.prography.backend.domain.cohortmember.entity;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * packageName    : com.prography.backend.domain.cohortmember.entity<br>
 * fileName       : CohortMemberEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 회원 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_cohort_member")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CohortMemberEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cohort_id", nullable = false)
    private CohortEntity cohort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private PartEntity part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @Column(nullable = false)
    private Long deposit;

    @Column(name = "excuse_count", nullable = false)
    private Integer excuseCount;

    public void updateDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public void updateExcuseCount(Integer excuseCount) {
        this.excuseCount = excuseCount;
    }

    public void updatePart(PartEntity part) {
        this.part = part;
    }

    public void updateTeam(TeamEntity team) {
        this.team = team;
    }
}
