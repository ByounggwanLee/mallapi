package com.skax.core.entity.member;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 역할 정보를 나타내는 엔티티 클래스
 * 
 * <p>시스템에서 사용되는 권한 역할을 정의합니다.</p>
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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    /**
     * 역할 이름 (예: ROLE_USER, ROLE_ADMIN)
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    /**
     * 역할 설명
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 역할 활성화 상태
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
