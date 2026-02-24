package com.prography.backend.domain.cohort.entity;

import com.prography.backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.prography.backend.domain.cohort.entity<br>
 * fileName       : CohortEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 기수 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_cohort")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CohortEntity extends BaseEntity {

    @Column(nullable = false)
    private Integer generation;

    @Column(nullable = false)
    private String name;
}
