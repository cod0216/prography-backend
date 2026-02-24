package com.prography.backend.domain.cohortmember.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.repository.PartRepository;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.domain.cohortmember.service<br>
 * fileName       : CohortMemberServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 회원 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class CohortMemberServiceTest {

    @Autowired
    private CohortMemberService cohortMemberService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void create_createsCohortMember() {
        // Given
        MemberEntity member = memberService.createMember("user1", passwordEncoder.encode("password123"), "홍길동", "010-1234-5678", MemberRole.MEMBER);
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        PartEntity part = partRepository.save(PartEntity.builder().name("SERVER").cohort(cohort).build());
        TeamEntity team = teamRepository.save(TeamEntity.builder().name("Team A").cohort(cohort).build());

        // When
        CohortMemberEntity cohortMember = cohortMemberService.create(member, cohort, part, team, 100_000L);

        // Then
        assertThat(cohortMember.getId()).isNotNull();
        assertThat(cohortMember.getMember().getId()).isEqualTo(member.getId());
        assertThat(cohortMember.getDeposit()).isEqualTo(100_000L);
        assertThat(cohortMember.getExcuseCount()).isEqualTo(0);
    }
}
