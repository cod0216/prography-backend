package com.prography.backend.domain.member.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.repository.CohortRepository;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.repository.CohortMemberRepository;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.entity.DepositHistoryEntity;
import com.prography.backend.domain.deposit.repository.DepositHistoryRepository;
import com.prography.backend.domain.member.dto.request.AdminMemberCreateRequest;
import com.prography.backend.domain.member.dto.request.AdminMemberUpdateRequest;
import com.prography.backend.domain.member.dto.request.MemberDashboardQuery;
import com.prography.backend.domain.member.dto.response.MemberAdminResponse;
import com.prography.backend.domain.member.dto.response.MemberDashboardItemResponse;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.repository.PartRepository;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.repository.TeamRepository;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName    : com.prography.backend.domain.member.service<br>
 * fileName       : MemberAdminFacadeServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 회원 파사드 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class MemberAdminFacadeServiceTest {

    @Autowired
    private MemberAdminFacadeService memberAdminFacadeService;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CohortMemberRepository cohortMemberRepository;

    @Autowired
    private CohortMemberService cohortMemberService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Test
    void createMember_createsCohortMemberAndDepositHistory() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        PartEntity part = partRepository.save(PartEntity.builder().name("SERVER").cohort(cohort).build());
        TeamEntity team = teamRepository.save(TeamEntity.builder().name("Team A").cohort(cohort).build());

        AdminMemberCreateRequest request = buildCreateRequest("user1", "password123", "홍길동", "010-1234-5678", cohort.getId(), part.getId(), team.getId());

        // When
        MemberAdminResponse response = memberAdminFacadeService.createMember(request);

        // Then
        CohortMemberEntity cohortMember = cohortMemberRepository.findFirstByMemberIdOrderByIdDesc(response.getId()).orElseThrow();
        assertThat(cohortMember.getDeposit()).isEqualTo(PolicyConstants.INITIAL_DEPOSIT);

        List<DepositHistoryEntity> histories = depositHistoryRepository.findByCohortMemberIdOrderByCreatedAtAsc(cohortMember.getId());
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getAmount()).isEqualTo(PolicyConstants.INITIAL_DEPOSIT);
        assertThat(histories.get(0).getBalanceAfter()).isEqualTo(PolicyConstants.INITIAL_DEPOSIT);
    }

    @Test
    void getMemberDashboard_filtersByGeneration() {
        // Given
        CohortEntity cohort10 = cohortRepository.save(CohortEntity.builder().generation(10).name("10기").build());
        CohortEntity cohort11 = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());

        AdminMemberCreateRequest request1 = buildCreateRequest("user10", "password123", "홍길동", "010-1111-1111", cohort10.getId(), null, null);
        AdminMemberCreateRequest request2 = buildCreateRequest("user11", "password123", "성춘향", "010-2222-2222", cohort11.getId(), null, null);
        memberAdminFacadeService.createMember(request1);
        memberAdminFacadeService.createMember(request2);

        MemberDashboardQuery query = MemberDashboardQuery.builder()
                .page(0)
                .size(10)
                .generation(11)
                .build();

        // When
        PageResponse<MemberDashboardItemResponse> result = memberAdminFacadeService.getMemberDashboard(query);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getGeneration()).isEqualTo(11);
    }

    @Test
    void updateMember_updatesPartAndTeam() {
        // Given
        CohortEntity cohort = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        PartEntity part1 = partRepository.save(PartEntity.builder().name("SERVER").cohort(cohort).build());
        PartEntity part2 = partRepository.save(PartEntity.builder().name("WEB").cohort(cohort).build());
        TeamEntity team1 = teamRepository.save(TeamEntity.builder().name("Team A").cohort(cohort).build());
        TeamEntity team2 = teamRepository.save(TeamEntity.builder().name("Team B").cohort(cohort).build());

        AdminMemberCreateRequest request = buildCreateRequest("user2", "password123", "이몽룡", "010-3333-3333", cohort.getId(), part1.getId(), team1.getId());
        MemberAdminResponse created = memberAdminFacadeService.createMember(request);

        AdminMemberUpdateRequest updateRequest = buildUpdateRequest(null, null, null, part2.getId(), team2.getId());

        // When
        MemberAdminResponse updated = memberAdminFacadeService.updateMember(created.getId(), updateRequest);

        // Then
        assertThat(updated.getPartName()).isEqualTo("WEB");
        assertThat(updated.getTeamName()).isEqualTo("Team B");
    }

    @Test
    void updateMember_withoutCohort_updatesCurrentGenerationOnly() {
        // Given
        CohortEntity cohort10 = cohortRepository.save(CohortEntity.builder().generation(10).name("10기").build());
        CohortEntity cohort11 = cohortRepository.save(CohortEntity.builder().generation(11).name("11기").build());
        PartEntity part10 = partRepository.save(PartEntity.builder().name("SERVER").cohort(cohort10).build());
        PartEntity part11 = partRepository.save(PartEntity.builder().name("WEB").cohort(cohort11).build());
        TeamEntity team11 = teamRepository.save(TeamEntity.builder().name("Team A").cohort(cohort11).build());

        AdminMemberCreateRequest request = buildCreateRequest("user3", "password123", "임꺽정", "010-4444-4444", cohort11.getId(), part11.getId(), team11.getId());
        MemberAdminResponse created = memberAdminFacadeService.createMember(request);

        CohortMemberEntity cohortMember10 = cohortMemberService.create(
                memberService.getById(created.getId()),
                cohort10,
                part10,
                null,
                PolicyConstants.INITIAL_DEPOSIT
        );

        // When
        AdminMemberUpdateRequest updateRequest = buildUpdateRequest(null, null, null, part11.getId(), team11.getId());
        MemberAdminResponse updated = memberAdminFacadeService.updateMember(created.getId(), updateRequest);

        // Then
        assertThat(updated.getGeneration()).isEqualTo(11);
        assertThat(updated.getPartName()).isEqualTo("WEB");
        assertThat(cohortMember10.getPart().getName()).isEqualTo("SERVER");
    }

    private AdminMemberCreateRequest buildCreateRequest(String loginId, String password, String name, String phone,
                                                        Long cohortId, Long partId, Long teamId) {
        AdminMemberCreateRequest request = new AdminMemberCreateRequest();
        setField(request, "loginId", loginId);
        setField(request, "password", password);
        setField(request, "name", name);
        setField(request, "phone", phone);
        setField(request, "cohortId", cohortId);
        setField(request, "partId", partId);
        setField(request, "teamId", teamId);
        return request;
    }

    private AdminMemberUpdateRequest buildUpdateRequest(String name, String phone, Long cohortId, Long partId, Long teamId) {
        AdminMemberUpdateRequest request = new AdminMemberUpdateRequest();
        setField(request, "name", name);
        setField(request, "phone", phone);
        setField(request, "cohortId", cohortId);
        setField(request, "partId", partId);
        setField(request, "teamId", teamId);
        return request;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
