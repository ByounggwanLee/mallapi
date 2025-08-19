package com.skax.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 공통 속성을 정의하는 기본 클래스
 * 
 * <p>이 클래스는 모든 엔티티에서 공통으로 사용되는 필드들을 정의합니다:</p>
 * <ul>
 *   <li>생성일시 (createdAt)</li>
 *   <li>수정일시 (updatedAt)</li>
 * </ul>
 * 
 * <p>Spring Data JPA의 Auditing 기능을 활용하여 자동으로 생성일시와 수정일시가 관리됩니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * 엔티티 생성일시
     * 엔티티가 처음 저장될 때 자동으로 설정됩니다.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 최종 수정일시
     * 엔티티가 수정될 때마다 자동으로 갱신됩니다.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
