package com.skax.core.repository.member;

import com.skax.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 정보 접근을 위한 Repository 인터페이스
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원을 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 회원 정보
     */
    Optional<Member> findByEmail(String email);

    /**
     * OAuth2 제공자와 제공자 ID로 회원을 조회합니다.
     * 
     * @param provider OAuth2 제공자
     * @param providerId 제공자 ID
     * @return 회원 정보
     */
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    /**
     * 이메일 존재 여부를 확인합니다.
     * 
     * @param email 회원 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 활성화된 회원을 이메일로 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 활성화된 회원 정보
     */
    Optional<Member> findByEmailAndIsActiveTrue(String email);

    /**
     * 회원과 역할 정보를 함께 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 회원 정보 (역할 포함)
     */
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberRoles mr LEFT JOIN FETCH mr.role WHERE m.email = :email")
    Optional<Member> findByEmailWithRoles(@Param("email") String email);
}
