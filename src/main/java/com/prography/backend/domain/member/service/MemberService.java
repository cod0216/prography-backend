package com.prography.backend.domain.member.service;

import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.entity.MemberStatus;
import com.prography.backend.domain.member.repository.MemberRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.member.service<br>
 * fileName       : MemberService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberEntity createMember(String loginId, String encodedPassword, String name, String phone, MemberRole role) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(StatusCode.DUPLICATE_LOGIN_ID);
        }

        MemberEntity member = MemberEntity.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .phone(phone)
                .status(MemberStatus.ACTIVE)
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberEntity getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findOptionalByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public void withdrawMember(Long id) {
        MemberEntity member = getById(id);
        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new CustomException(StatusCode.MEMBER_ALREADY_WITHDRAWN);
        }
        member.withdraw();
    }
}
