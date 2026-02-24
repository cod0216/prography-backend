package com.prography.backend.domain.part.repository;

import com.prography.backend.domain.part.entity.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.prography.backend.domain.part.repository<br>
 * fileName       : PartRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 파트 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
public interface PartRepository extends JpaRepository<PartEntity, Long> {
}
