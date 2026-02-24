package com.prography.backend.domain.auth.service;

import com.prography.backend.domain.auth.dto.request.LoginRequest;
import com.prography.backend.domain.member.dto.response.MemberResponse;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.prography.backend.domain.auth.service<br>
 * fileName       : AuthFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 인증 응답 매핑을 처리하는 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthFacadeService {

    private final AuthService authService;
    private final MemberMapper memberMapper;

    public MemberResponse login(LoginRequest request) {
        MemberEntity member = authService.login(request);
        return memberMapper.toMemberResponse(member);
    }
}
