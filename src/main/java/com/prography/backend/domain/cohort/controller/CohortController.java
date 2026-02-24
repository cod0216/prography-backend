package com.prography.backend.domain.cohort.controller;

import com.prography.backend.domain.cohort.dto.response.CohortDetailResponse;
import com.prography.backend.domain.cohort.dto.response.CohortSummaryResponse;
import com.prography.backend.domain.cohort.service.CohortFacadeService;
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
 * packageName    : com.prography.backend.domain.cohort.controller<br>
 * fileName       : CohortController.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 조회 API를 처리하는 컨트롤러 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/cohorts")
public class CohortController {

    private final CohortFacadeService cohortFacadeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CohortSummaryResponse>>> getCohorts() {
        return ResponseUtility.success(cohortFacadeService.getCohortSummaries());
    }

    @GetMapping("/{cohortId}")
    public ResponseEntity<ApiResponse<CohortDetailResponse>> getCohortDetail(@PathVariable Long cohortId) {
        return ResponseUtility.success(cohortFacadeService.getCohortDetail(cohortId));
    }
}
