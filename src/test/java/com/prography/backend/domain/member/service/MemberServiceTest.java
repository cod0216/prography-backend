package com.prography.backend.domain.member.service;

import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.entity.MemberRole;
import com.prography.backend.domain.member.entity.MemberStatus;
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
 * packageName    : com.prography.backend.domain.member.service<br>
 * fileName       : MemberServiceTest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 서비스 테스트 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createMember_savesMember() {
        // Given
        String loginId = "user1";
        String password = passwordEncoder.encode("password123");

        // When
        MemberEntity member = memberService.createMember(loginId, password, "홍길동", "010-1234-5678", MemberRole.MEMBER);

        // Then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getLoginId()).isEqualTo(loginId);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getRole()).isEqualTo(MemberRole.MEMBER);
    }

    @Test
    void createMember_duplicateLoginId_throwsException() {
        // Given
        String loginId = "user1";
        String password = passwordEncoder.encode("password123");
        memberService.createMember(loginId, password, "홍길동", "010-1234-5678", MemberRole.MEMBER);

        // When & Then
        assertThatThrownBy(() -> memberService.createMember(loginId, password, "홍길동", "010-0000-0000", MemberRole.MEMBER))
                .isInstanceOf(CustomException.class)
                .extracting("statusCode")
                .isEqualTo(StatusCode.DUPLICATE_LOGIN_ID);
    }

    @Test
    void withdrawMember_updatesStatus() {
        // Given
        MemberEntity member = memberService.createMember("user2", passwordEncoder.encode("password123"), "이몽룡", "010-2222-3333", MemberRole.MEMBER);

        // When
        memberService.withdrawMember(member.getId());

        // Then
        MemberEntity updated = memberService.getById(member.getId());
        assertThat(updated.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
    }
}
