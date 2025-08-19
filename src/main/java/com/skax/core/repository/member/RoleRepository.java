package com.skax.core.repository.member;

import com.skax.core.entity.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 역할 정보 접근을 위한 Repository 인터페이스
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
