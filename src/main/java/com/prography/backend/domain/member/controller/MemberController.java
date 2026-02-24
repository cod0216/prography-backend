package com.prography.backend.domain.member.controller;

import com.prography.backend.domain.member.dto.response.MemberResponse;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.member.mapper.MemberMapper;
import com.prography.backend.domain.member.service.MemberService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.prography.backend.domain.member.controller<br>
 * fileName       : MemberController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 조회 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long id) {
        MemberEntity member = memberService.getById(id);
        return ResponseUtility.success(memberMapper.toMemberResponse(member));
    }
}
