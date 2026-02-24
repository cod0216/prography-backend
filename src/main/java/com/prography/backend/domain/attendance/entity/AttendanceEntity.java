package com.prography.backend.domain.attendance.entity;

import com.prography.backend.domain.member.entity.MemberEntity;
import com.prography.backend.domain.session.entity.SessionEntity;
import com.prography.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.prography.backend.domain.attendance.entity<br>
 * fileName       : AttendanceEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 출결 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(
        name = "tb_attendance",
        uniqueConstraints = @UniqueConstraint(name = "uq_attendance_session_member", columnNames = {"session_id", "member_id"})
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private SessionEntity session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "qrcode_id")
    private Long qrCodeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "penalty_amount", nullable = false)
    private Long penaltyAmount;

    @Column
    private String reason;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    public void updateByAdmin(AttendanceStatus status, Integer lateMinutes, Long penaltyAmount, String reason) {
        this.status = status;
        this.lateMinutes = lateMinutes;
        this.penaltyAmount = penaltyAmount;
        if (reason != null) {
            this.reason = reason;
        }
    }
}
