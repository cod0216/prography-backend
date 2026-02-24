package com.prography.backend.domain.part.mapper;

import com.prography.backend.domain.part.dto.response.PartSummaryResponse;
import com.prography.backend.domain.part.entity.PartEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.part.mapper<br>
 * fileName       : PartMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 파트 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Component
public class PartMapper {

    public PartSummaryResponse toSummaryResponse(PartEntity part) {
        if (part == null) {
            return null;
        }
        return PartSummaryResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .build();
    }
}
