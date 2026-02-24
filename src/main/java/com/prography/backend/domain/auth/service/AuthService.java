package com.prography.backend.domain.auth.service;

import com.prography.backend.domain.auth.dto.request.LoginRequest;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberStatus;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.prography.backend.domain.auth.service<br>
 * fileName       : AuthService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberEntity login(LoginRequest request) {
        MemberEntity member = memberService.findOptionalByLoginId(request.getLoginId())
                .orElseThrow(() -> new CustomException(StatusCode.LOGIN_FAILED));

        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new CustomException(StatusCode.MEMBER_WITHDRAWN);
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(StatusCode.LOGIN_FAILED);
        }

        return member;
    }
}
