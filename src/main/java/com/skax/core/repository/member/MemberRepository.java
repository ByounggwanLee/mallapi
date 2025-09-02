package com.skax.core.repository.member;

import com.skax.core.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>회원 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>이메일 기반 회원 조회</li>
 *   <li>OAuth2 소셜 로그인 회원 조회</li>
 *   <li>닉네임 기반 회원 조회</li>
 *   <li>소셜 로그인 여부별 조회</li>
 *   <li>권한별 회원 조회</li>
 * </ul>
 * 
 * <p>Spring Security와 연동하여 인증 및 권한 부여에 활용됩니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

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
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberRoleList WHERE m.email = :email")
    Optional<Member> findByEmailWithRoles(@Param("email") String email);
    
    /**
     * 닉네임 존재 여부를 확인합니다.
     * 
     * @param nickname 닉네임
     * @return 존재 여부
     */
    boolean existsByNickname(String nickname);
    
    /**
     * 닉네임을 포함하는 회원을 검색합니다 (대소문자 무시).
     * 
     * @param nickname 검색할 닉네임
     * @param pageable 페이징 정보
     * @return 검색된 회원 목록
     */
    Page<Member> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
    
    /**
     * 소셜 로그인 여부로 회원을 조회합니다.
     * 
     * @param social 소셜 로그인 여부
     * @param pageable 페이징 정보
     * @return 조건에 맞는 회원 목록
     */
    Page<Member> findBySocial(boolean social, Pageable pageable);
    
    /**
     * 소셜 로그인 회원 수를 조회합니다.
     * 
     * @param social 소셜 로그인 여부
     * @return 회원 수
     */
    long countBySocial(boolean social);
}
