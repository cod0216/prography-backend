package com.prography.backend.domain.member.controller;

import com.prography.backend.domain.member.dto.request.AdminMemberCreateRequest;
import com.prography.backend.domain.member.dto.request.AdminMemberUpdateRequest;
import com.prography.backend.domain.member.dto.request.MemberDashboardQuery;
import com.prography.backend.domain.member.dto.response.MemberAdminResponse;
import com.prography.backend.domain.member.dto.response.MemberDashboardItemResponse;
import com.prography.backend.domain.member.dto.response.MemberWithdrawResponse;
import com.prography.backend.domain.member.entity.MemberStatus;
import com.prography.backend.domain.member.service.MemberAdminFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.response.PageResponse;
import com.prography.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.prography.backend.domain.member.controller<br>
 * fileName       : AdminMemberController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-25<br>
 * description    : 관리자 회원 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-25         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
public class AdminMemberController {

    private final MemberAdminFacadeService memberAdminFacadeService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberAdminResponse>> createMember(@RequestBody @Valid AdminMemberCreateRequest request) {
        return ResponseUtility.created(memberAdminFacadeService.createMember(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MemberDashboardItemResponse>>> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) String partName,
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) MemberStatus status
    ) {
        MemberDashboardQuery query = MemberDashboardQuery.builder()
                .page(page)
                .size(size)
                .searchType(searchType)
                .searchValue(searchValue)
                .generation(generation)
                .partName(partName)
                .teamName(teamName)
                .status(status)
                .build();

        return ResponseUtility.success(memberAdminFacadeService.getMemberDashboard(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberAdminResponse>> getMemberDetail(@PathVariable Long id) {
        return ResponseUtility.success(memberAdminFacadeService.getMemberDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberAdminResponse>> updateMember(
            @PathVariable Long id,
            @RequestBody AdminMemberUpdateRequest request
    ) {
        return ResponseUtility.success(memberAdminFacadeService.updateMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberWithdrawResponse>> deleteMember(@PathVariable Long id) {
        return ResponseUtility.success(memberAdminFacadeService.withdrawMember(id));
    }
}
