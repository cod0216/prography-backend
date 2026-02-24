package com.prography.backend.domain.deposit.controller;

import com.prography.backend.domain.deposit.dto.response.DepositHistoryResponse;
import com.prography.backend.domain.deposit.service.DepositHistoryAdminFacadeService;
import com.prography.backend.global.response.ApiResponse;
import com.prography.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.deposit.controller<br>
 * fileName       : AdminDepositController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 보증금 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/cohort-members")
public class AdminDepositController {

    private final DepositHistoryAdminFacadeService depositHistoryAdminFacadeService;

    @GetMapping("/{cohortMemberId}/deposits")
    public ResponseEntity<ApiResponse<List<DepositHistoryResponse>>> getDepositHistory(@PathVariable Long cohortMemberId) {
        return ResponseUtility.success(depositHistoryAdminFacadeService.getDepositHistories(cohortMemberId));
    }
}
