package com.prography.backend.domain.session.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * packageName    : com.prography.backend.domain.session.dto.request<br>
 * fileName       : AdminSessionCreateRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 관리자 일정 생성 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Getter
public class AdminSessionCreateRequest {

    @NotBlank(message = "title은 필수입니다.")
    private String title;

    @NotNull(message = "date는 필수입니다.")
    private LocalDate date;

    @NotNull(message = "time은 필수입니다.")
    private LocalTime time;

    @NotBlank(message = "location은 필수입니다.")
    private String location;
}
