package com.prography.backend.domain.member.mapper;

import com.prography.backend.domain.member.dto.response.MemberResponse;
import com.prography.backend.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.member.mapper<br>
 * fileName       : MemberMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class MemberMapper {

    public MemberResponse toMemberResponse(MemberEntity member) {
        if (member == null) {
            return null;
        }
        return MemberResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .phone(member.getPhone())
                .status(member.getStatus())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
