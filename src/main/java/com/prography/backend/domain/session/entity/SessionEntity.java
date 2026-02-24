package com.prography.backend.domain.session.entity;

import com.prography.backend.domain.cohort.entity.CohortEntity;
import com.prography.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * packageName    : com.prography.backend.domain.session.entity<br>
 * fileName       : SessionEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 일정 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 * 2026-02-24         cod0216             일정 수정 및 취소 처리 메서드 추가<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_session")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cohort_id", nullable = false)
    private CohortEntity cohort;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    public void updateInfo(String title, LocalDate date, LocalTime time, String location) {
        if (title != null) {
            this.title = title;
        }
        if (date != null) {
            this.date = date;
        }
        if (time != null) {
            this.time = time;
        }
        if (location != null) {
            this.location = location;
        }
    }

    public void updateStatus(SessionStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void cancel() {
        this.status = SessionStatus.CANCELLED;
    }
}
