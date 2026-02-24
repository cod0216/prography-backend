package com.prography.backend.domain.auth.service;

import com.prography.backend.domain.auth.dto.request.LoginRequest;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.entity.MemberStatus;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.prography.backend.domain.auth.service<br>
 * fileName       : AuthServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 인증 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void login_success() {
        // Given
        String rawPassword = "password123";
        MemberEntity member = memberService.createMember("admin", passwordEncoder.encode(rawPassword), "관리자", "010-0000-0000", MemberRole.ADMIN);
        LoginRequest request = buildRequest("admin", rawPassword);

        // When
        MemberEntity result = authService.login(request);

        // Then
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getLoginId()).isEqualTo("admin");
    }

    @Test
    void login_wrongPassword_throwsException() {
        // Given
        memberService.createMember("user1", passwordEncoder.encode("password123"), "홍길동", "010-1234-5678", MemberRole.MEMBER);
        LoginRequest request = buildRequest("user1", "wrong");

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.LOGIN_FAILED);
    }

    @Test
    void login_withdrawnMember_throwsException() {
        // Given
        MemberEntity member = memberService.createMember("user2", passwordEncoder.encode("password123"), "성춘향", "010-2222-3333", MemberRole.MEMBER);
        memberService.withdrawMember(member.getId());
        LoginRequest request = buildRequest("user2", "password123");

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.MEMBER_WITHDRAWN);
    }

    private LoginRequest buildRequest(String loginId, String password) {
        LoginRequest request = new LoginRequest();
        try {
            java.lang.reflect.Field loginIdField = LoginRequest.class.getDeclaredField("loginId");
            java.lang.reflect.Field passwordField = LoginRequest.class.getDeclaredField("password");
            loginIdField.setAccessible(true);
            passwordField.setAccessible(true);
            loginIdField.set(request, loginId);
            passwordField.set(request, password);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        return request;
    }
}
