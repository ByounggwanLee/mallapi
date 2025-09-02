package com.skax.core.repository.member;

import com.skax.core.entity.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>시스템 역할 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>역할 이름 기반 조회</li>
 *   <li>활성화 상태별 역할 조회</li>
 *   <li>권한 체계 관리</li>
 * </ul>
 * 
 * <p>Spring Security와 연동하여 권한 기반 접근 제어(RBAC)에 활용됩니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 역할 이름으로 역할을 조회합니다.
     * 
     * @param roleName 역할 이름
     * @return 역할 정보
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * 활성화된 역할을 이름으로 조회합니다.
     * 
     * @param roleName 역할 이름
     * @return 활성화된 역할 정보
     */
    Optional<Role> findByRoleNameAndIsActiveTrue(String roleName);

    /**
     * 역할 이름 존재 여부를 확인합니다.
     * 
     * @param roleName 역할 이름
     * @return 존재 여부
     */
    boolean existsByRoleName(String roleName);
}
