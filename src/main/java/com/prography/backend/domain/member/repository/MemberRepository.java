package com.prography.backend.domain.member.repository;

import com.prography.backend.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.prography.backend.domain.member.repository<br>
 * fileName       : MemberRepository.java<br>
 * author         : cod0216 <br>
 * date           : 2026-02-24<br>
 * description    : 회원 레포지토리 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2026-02-24         cod0216             최초생성<br>
 */
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
