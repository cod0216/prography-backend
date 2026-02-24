package com.prography.backend.domain.member.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.service.CohortService;
import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.cohortmember.service.CohortMemberService;
import com.prography.backend.domain.deposit.service.DepositHistoryService;
import com.prography.backend.domain.member.dto.request.AdminMemberCreateRequest;
import com.prography.backend.domain.member.dto.request.AdminMemberUpdateRequest;
import com.prography.backend.domain.member.dto.request.MemberDashboardQuery;
import com.prography.backend.domain.member.dto.response.MemberAdminResponse;
import com.prography.backend.domain.member.dto.response.MemberDashboardItemResponse;
import com.prography.backend.domain.member.dto.response.MemberWithdrawResponse;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.mapper.MemberAdminMapper;
import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.service.PartService;
import com.prography.backend.domain.team.entity.TeamEntity;
import com.prography.backend.domain.team.service.TeamService;
import com.prography.backend.global.common.PolicyConstants;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import com.prography.backend.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * packageName    : com.prography.backend.domain.member.service<br>
 * fileName       : MemberAdminFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 회원 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAdminFacadeService {

    private final MemberService memberService;
    private final CohortService cohortService;
    private final PartService partService;
    private final TeamService teamService;
    private final CohortMemberService cohortMemberService;
    private final DepositHistoryService depositHistoryService;
    private final MemberAdminMapper memberAdminMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberAdminResponse createMember(AdminMemberCreateRequest request) {
        CohortEntity cohort = cohortService.getById(request.getCohortId());
        PartEntity part = resolvePart(request.getPartId());
        TeamEntity team = resolveTeam(request.getTeamId());

        MemberEntity member = memberService.createMember(
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhone(),
                MemberRole.MEMBER
        );

        CohortMemberEntity cohortMember = cohortMemberService.create(
                member,
                cohort,
                part,
                team,
                PolicyConstants.INITIAL_DEPOSIT
        );

        depositHistoryService.createInitial(
                cohortMember,
                PolicyConstants.INITIAL_DEPOSIT,
                PolicyConstants.INITIAL_DEPOSIT_DESCRIPTION
        );

        return memberAdminMapper.toAdminResponse(member, cohortMember);
    }

    @Transactional(readOnly = true)
    public PageResponse<MemberDashboardItemResponse> getMemberDashboard(MemberDashboardQuery query) {
        validateSearchQuery(query.getSearchType(), query.getSearchValue());

        List<MemberEntity> members = memberService.searchMembers(
                query.getStatus(),
                query.getSearchType(),
                query.getSearchValue()
        );

        List<MemberDashboardItemResponse> items = members.stream()
                .map(this::toDashboardItem)
                .filter(item -> matchesGeneration(item, query.getGeneration()))
                .filter(item -> matchesPartName(item, query.getPartName()))
                .filter(item -> matchesTeamName(item, query.getTeamName()))
                .collect(Collectors.toList());

        return paginate(items, query.getPage(), query.getSize());
    }

    @Transactional(readOnly = true)
    public MemberAdminResponse getMemberDetail(Long memberId) {
        MemberEntity member = memberService.getById(memberId);
        CohortMemberEntity cohortMember = cohortMemberService.findLatestByMemberId(memberId).orElse(null);
        return memberAdminMapper.toAdminResponse(member, cohortMember);
    }

    public MemberAdminResponse updateMember(Long memberId, AdminMemberUpdateRequest request) {
        MemberEntity member = memberService.updateMember(memberId, request.getName(), request.getPhone());
        CohortMemberEntity cohortMember = updateCohortMember(member, request);
        return memberAdminMapper.toAdminResponse(member, cohortMember);
    }

    public MemberWithdrawResponse withdrawMember(Long memberId) {
        MemberEntity member = memberService.withdrawMember(memberId);
        return memberAdminMapper.toWithdrawResponse(member);
    }

    private CohortMemberEntity updateCohortMember(MemberEntity member, AdminMemberUpdateRequest request) {
        if (request.getCohortId() != null) {
            CohortEntity cohort = cohortService.getById(request.getCohortId());
            Optional<CohortMemberEntity> existing = cohortMemberService.findByMemberAndCohort(member.getId(), cohort.getId());
            if (existing.isPresent()) {
                updatePartAndTeam(existing.get(), request.getPartId(), request.getTeamId());
                return existing.get();
            }

            PartEntity part = resolvePart(request.getPartId());
            TeamEntity team = resolveTeam(request.getTeamId());
            CohortMemberEntity created = cohortMemberService.create(member, cohort, part, team, PolicyConstants.INITIAL_DEPOSIT);
            depositHistoryService.createInitial(created, PolicyConstants.INITIAL_DEPOSIT, PolicyConstants.INITIAL_DEPOSIT_DESCRIPTION);
            return created;
        }

        Optional<CohortMemberEntity> latest = cohortMemberService.findLatestByMemberId(member.getId());
        if (request.getPartId() == null && request.getTeamId() == null) {
            return latest.orElse(null);
        }

        CohortEntity currentCohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        CohortMemberEntity cohortMember = cohortMemberService.findByMemberAndCohort(member.getId(), currentCohort.getId())
                .orElseThrow(() -> new CustomException(StatusCode.COHORT_MEMBER_NOT_FOUND));
        updatePartAndTeam(cohortMember, request.getPartId(), request.getTeamId());
        return cohortMember;
    }

    private void updatePartAndTeam(CohortMemberEntity cohortMember, Long partId, Long teamId) {
        if (partId != null) {
            cohortMember.updatePart(resolvePart(partId));
        }
        if (teamId != null) {
            cohortMember.updateTeam(resolveTeam(teamId));
        }
    }

    private PartEntity resolvePart(Long partId) {
        if (partId == null) {
            return null;
        }
        return partService.getById(partId);
    }

    private TeamEntity resolveTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }
        return teamService.getById(teamId);
    }

    private MemberDashboardItemResponse toDashboardItem(MemberEntity member) {
        CohortMemberEntity cohortMember = cohortMemberService.findLatestByMemberId(member.getId()).orElse(null);
        return memberAdminMapper.toDashboardItem(member, cohortMember);
    }

    private void validateSearchQuery(String searchType, String searchValue) {
        if (searchType != null && (searchValue == null || searchValue.isBlank())) {
            throw new CustomException(StatusCode.INVALID_INPUT);
        }
    }

    private boolean matchesGeneration(MemberDashboardItemResponse item, Integer generation) {
        if (generation == null) {
            return true;
        }
        return generation.equals(item.getGeneration());
    }

    private boolean matchesPartName(MemberDashboardItemResponse item, String partName) {
        if (partName == null || partName.isBlank()) {
            return true;
        }
        return partName.equals(item.getPartName());
    }

    private boolean matchesTeamName(MemberDashboardItemResponse item, String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return true;
        }
        return teamName.equals(item.getTeamName());
    }

    private PageResponse<MemberDashboardItemResponse> paginate(List<MemberDashboardItemResponse> items, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : size;
        int totalElements = items.size();
        int totalPages = (int) Math.ceil((double) totalElements / safeSize);

        int fromIndex = safePage * safeSize;
        if (fromIndex >= totalElements) {
            return PageResponse.<MemberDashboardItemResponse>builder()
                    .content(new ArrayList<>())
                    .page(safePage)
                    .size(safeSize)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        }

        int toIndex = Math.min(fromIndex + safeSize, totalElements);
        List<MemberDashboardItemResponse> content = items.subList(fromIndex, toIndex);

        return PageResponse.<MemberDashboardItemResponse>builder()
                .content(content)
                .page(safePage)
                .size(safeSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}
