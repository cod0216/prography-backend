package com.prography.backend.domain.attendance.repository;

import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.repository<br>
 * fileName       : AttendanceRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 출결 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    boolean existsBySessionIdAndMemberId(Long sessionId, Long memberId);

    List<AttendanceEntity> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    List<AttendanceEntity> findByMemberIdOrderByCreatedAtAsc(Long memberId);

    List<AttendanceEntity> findByMemberIdIn(Collection<Long> memberIds);
}
