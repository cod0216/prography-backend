package com.prography.backend.domain.attendance.mapper;

import com.prography.backend.domain.attendance.dto.response.AttendanceResponse;
import com.prography.backend.domain.attendance.dto.response.MemberAttendanceResponse;
import com.prography.backend.domain.attendance.entity.AttendanceEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.attendance.mapper<br>
 * fileName       : AttendanceMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 출결 응답 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             회원 출결 응답 매핑 추가<br>
 */
@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(AttendanceEntity attendance) {
        if (attendance == null) {
            return null;
        }

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .sessionId(attendance.getSession().getId())
                .memberId(attendance.getMember().getId())
                .status(attendance.getStatus())
                .lateMinutes(attendance.getLateMinutes())
                .penaltyAmount(attendance.getPenaltyAmount())
                .reason(attendance.getReason())
                .checkedInAt(attendance.getCheckedInAt())
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }
    public MemberAttendanceResponse toMemberResponse(AttendanceEntity attendance) {
        if (attendance == null) {
            return null;
        }

        return MemberAttendanceResponse.builder()
                .id(attendance.getId())
                .sessionId(attendance.getSession().getId())
                .sessionTitle(attendance.getSession().getTitle())
                .status(attendance.getStatus())
                .lateMinutes(attendance.getLateMinutes())
                .penaltyAmount(attendance.getPenaltyAmount())
                .reason(attendance.getReason())
                .checkedInAt(attendance.getCheckedInAt())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
