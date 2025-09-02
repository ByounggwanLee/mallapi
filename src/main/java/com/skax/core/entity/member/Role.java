package com.skax.core.entity.member;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 역할 정보를 나타내는 엔티티 클래스
 * 
 * <p>시스템에서 사용되는 사용자 권한 역할을 정의하고 관리합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>역할 고유 식별자</li>
 *   <li>역할 이름 (ROLE_USER, ROLE_ADMIN 등)</li>
 *   <li>역할 설명</li>
 *   <li>활성화 상태</li>
 * </ul>
 * 
 * <p>Spring Security와 연동하여 권한 기반 접근 제어에 사용됩니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "role_name", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    /**
     * 역할 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    /**
     * 역할 이름
     * Spring Security 규칙에 따라 'ROLE_' 접두사로 시작합니다.
     * 예: ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    /**
     * 역할 설명
     * 해당 역할에 대한 상세한 설명입니다.
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 역할 활성화 상태
     * true: 활성화된 역할, false: 비활성화된 역할
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
