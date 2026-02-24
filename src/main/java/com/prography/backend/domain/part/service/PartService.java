package com.prography.backend.domain.part.service;

import com.prography.backend.domain.part.entity.PartEntity;
import com.prography.backend.domain.part.repository.PartRepository;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.prography.backend.domain.part.service<br>
 * fileName       : PartService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 파트 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public PartEntity getById(Long id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.PART_NOT_FOUND));
    }
}
