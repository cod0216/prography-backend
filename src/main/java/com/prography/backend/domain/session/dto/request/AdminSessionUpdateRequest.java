package com.prography.backend.domain.session.dto.request;

import com.prography.backend.domain.session.entity.SessionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * packageName    : com.prography.backend.domain.session.dto.request<br>
 * fileName       : AdminSessionUpdateRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정 수정 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
public class AdminSessionUpdateRequest {
    private String title;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private SessionStatus status;
}
