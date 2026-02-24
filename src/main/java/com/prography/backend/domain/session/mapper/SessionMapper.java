package com.prography.backend.domain.session.mapper;

import com.prography.backend.domain.session.dto.response.AdminSessionResponse;
import com.prography.backend.domain.session.dto.response.MemberSessionResponse;
import com.prography.backend.domain.session.dto.response.SessionAttendanceSummaryResponse;
import com.prography.backend.domain.session.entity.SessionEntity;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.prography.backend.domain.session.mapper<br>
 * fileName       : SessionMapper.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 일정 응답 매핑을 담당하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             회원 일정 응답 매핑 추가<br>
 */
@Component
public class SessionMapper {

    public AdminSessionResponse toAdminResponse(SessionEntity session, SessionAttendanceSummaryResponse summary, boolean qrActive) {
        if (session == null) {
            return null;
        }

        return AdminSessionResponse.builder()
                .id(session.getId())
                .cohortId(session.getCohort().getId())
                .title(session.getTitle())
                .date(session.getDate())
                .time(session.getTime())
                .location(session.getLocation())
                .status(session.getStatus())
                .attendanceSummary(summary)
                .qrActive(qrActive)
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    public MemberSessionResponse toMemberResponse(SessionEntity session) {
        if (session == null) {
            return null;
        }

        return MemberSessionResponse.builder()
                .id(session.getId())
                .title(session.getTitle())
                .date(session.getDate())
                .time(session.getTime())
                .location(session.getLocation())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }
}
