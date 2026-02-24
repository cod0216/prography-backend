package com.prography.backend.domain.member.mapper;

import com.prography.backend.domain.cohortmember.entity.CohortMemberEntity;
import com.prography.backend.domain.member.dto.response.MemberAdminResponse;
import com.prography.backend.domain.member.dto.response.MemberDashboardItemResponse;
import com.prography.backend.domain.member.dto.response.MemberWithdrawResponse;
import com.prography.backend.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.member.mapper<br>
 * fileName       : MemberAdminMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자용 회원 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Component
public class MemberAdminMapper {

    public MemberAdminResponse toAdminResponse(MemberEntity member, CohortMemberEntity cohortMember) {
        if (member == null) {
            return null;
        }
        return MemberAdminResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .phone(member.getPhone())
                .status(member.getStatus())
                .role(member.getRole())
                .generation(getGeneration(cohortMember))
                .partName(getPartName(cohortMember))
                .teamName(getTeamName(cohortMember))
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public MemberDashboardItemResponse toDashboardItem(MemberEntity member, CohortMemberEntity cohortMember) {
        if (member == null) {
            return null;
        }
        return MemberDashboardItemResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .phone(member.getPhone())
                .status(member.getStatus())
                .role(member.getRole())
                .generation(getGeneration(cohortMember))
                .partName(getPartName(cohortMember))
                .teamName(getTeamName(cohortMember))
                .deposit(getDeposit(cohortMember))
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public MemberWithdrawResponse toWithdrawResponse(MemberEntity member) {
        if (member == null) {
            return null;
        }
        return MemberWithdrawResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .status(member.getStatus())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    private Integer getGeneration(CohortMemberEntity cohortMember) {
        if (cohortMember == null || cohortMember.getCohort() == null) {
            return null;
        }
        return cohortMember.getCohort().getGeneration();
    }

    private String getPartName(CohortMemberEntity cohortMember) {
        if (cohortMember == null || cohortMember.getPart() == null) {
            return null;
        }
        return cohortMember.getPart().getName();
    }

    private String getTeamName(CohortMemberEntity cohortMember) {
        if (cohortMember == null || cohortMember.getTeam() == null) {
            return null;
        }
        return cohortMember.getTeam().getName();
    }

    private Long getDeposit(CohortMemberEntity cohortMember) {
        if (cohortMember == null) {
            return null;
        }
        return cohortMember.getDeposit();
    }
}
