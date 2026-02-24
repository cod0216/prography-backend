package com.prography.backend.domain.attendance.service;

import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import com.prography.backend.domain.attendance.entity.AttendanceStatus;
import com.prography.backend.domain.attendance.repository.AttendanceRepository;
import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.global.common.StatusCode;
import com.prography.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * packageName    : com.prography.backend.domain.attendance.service<br>
 * fileName       : AttendanceService.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 출결 관련 비즈니스 로직을 처리하는 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             QR 출석용 qrCodeId 저장 지원<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceEntity create(SessionEntity session, MemberEntity member, AttendanceStatus status,
                                   Integer lateMinutes, Long penaltyAmount, String reason, LocalDateTime checkedInAt,
                                   Long qrCodeId) {
        AttendanceEntity attendance = AttendanceEntity.builder()
                .session(session)
                .member(member)
                .qrCodeId(qrCodeId)
                .status(status)
                .lateMinutes(lateMinutes)
                .penaltyAmount(penaltyAmount)
                .reason(reason)
                .checkedInAt(checkedInAt)
                .build();

        return attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public AttendanceEntity getById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.ATTENDANCE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean existsBySessionAndMember(Long sessionId, Long memberId) {
        return attendanceRepository.existsBySessionIdAndMemberId(sessionId, memberId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceEntity> getBySessionId(Long sessionId) {
        return attendanceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceEntity> getByMemberId(Long memberId) {
        return attendanceRepository.findByMemberIdOrderByCreatedAtAsc(memberId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceEntity> getByMemberIds(Collection<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return List.of();
        }
        return attendanceRepository.findByMemberIdIn(memberIds);
    }
}
