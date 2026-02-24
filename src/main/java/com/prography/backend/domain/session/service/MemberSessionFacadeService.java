package com.prography.backend.domain.session.service;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.domain.cohort.service.CohortService;
import com.prography.backend.domain.session.dto.response.MemberSessionResponse;
import com.prography.backend.domain.session.entity.SessionStatus;
import com.prography.backend.domain.session.mapper.SessionMapper;
import com.prography.backend.global.common.PolicyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.prography.backend.domain.session.service<br>
 * fileName       : MemberSessionFacadeService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 일정 조회 API를 위한 파사드 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSessionFacadeService {

    private final CohortService cohortService;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;

    public List<MemberSessionResponse> getSessions() {
        CohortEntity cohort = cohortService.getByGeneration(PolicyConstants.CURRENT_GENERATION);
        return sessionService.getByCohortId(cohort.getId()).stream()
                .filter(session -> session.getStatus() != SessionStatus.CANCELLED)
                .map(sessionMapper::toMemberResponse)
                .toList();
    }
}
