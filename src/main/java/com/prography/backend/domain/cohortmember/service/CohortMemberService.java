package com.prography.backend.domain.cohortmember.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.repository.CohortMemberRepository;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.cohortmember.service<br>
 * fileName       : CohortMemberService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 회원 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CohortMemberService {

    private final CohortMemberRepository cohortMemberRepository;

    public CohortMemberEntity create(MemberEntity member, CohortEntity cohort, PartEntity part, TeamEntity team, Long deposit) {
        CohortMemberEntity cohortMember = CohortMemberEntity.builder()
                .member(member)
                .cohort(cohort)
                .part(part)
                .team(team)
                .deposit(deposit)
                .excuseCount(0)
                .build();

        return cohortMemberRepository.save(cohortMember);
    }

    @Transactional(readOnly = true)
    public Optional<CohortMemberEntity> findLatestByMemberId(Long memberId) {
        return cohortMemberRepository.findFirstByMemberIdOrderByIdDesc(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<CohortMemberEntity> findByMemberAndCohort(Long memberId, Long cohortId) {
        return cohortMemberRepository.findByMemberIdAndCohortId(memberId, cohortId);
    }

    @Transactional(readOnly = true)
    public CohortMemberEntity getById(Long id) {
        return cohortMemberRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.COHORT_MEMBER_NOT_FOUND));
    }
}
