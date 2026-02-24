package com.prography.backend.domain.cohort.mapper;

import com.prography.backend.domain.cohort.dto.response.CohortSummaryResponse;
import com.prography.backend.domain.cohort.entity.CohortEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.cohort.mapper<br>
 * fileName       : CohortMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class CohortMapper {

    public CohortSummaryResponse toSummaryResponse(CohortEntity cohort) {
        if (cohort == null) {
            return null;
        }
        return CohortSummaryResponse.builder()
                .id(cohort.getId())
                .generation(cohort.getGeneration())
                .name(cohort.getName())
                .createdAt(cohort.getCreatedAt())
                .build();
    }
}
