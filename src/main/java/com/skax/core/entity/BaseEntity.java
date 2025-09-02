package com.skax.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Soft Delete 기능을 제공하는 최상위 엔티티 베이스 클래스
 * BaseTimeEntity -> AuditableEntity -> BaseEntity 상속 구조의 최상위
 * 
 * <p>이 클래스는 모든 엔티티에서 공통으로 사용되는 기능들을 정의합니다:</p>
 * <ul>
 *   <li>생성일시, 수정일시 (BaseTimeEntity에서 상속)</li>
 *   <li>작성자, 수정자 (AuditableEntity에서 상속)</li>
 *   <li>Soft Delete 기능 (현재 클래스에서 제공)</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends AuditableEntity implements Serializable {
    
    /**
     * 논리적 삭제 플래그
     * true: 삭제된 상태, false: 활성 상태
     * 실제 데이터를 삭제하지 않고 논리적으로 삭제 처리합니다.
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * 엔티티를 논리적으로 삭제합니다.
     * 실제 데이터는 삭제되지 않고 deleted 플래그만 true로 변경됩니다.
     */
    public void softDelete() {
        this.deleted = true;
    }

    /**
     * 엔티티의 논리적 삭제를 복원합니다.
     * deleted 플래그를 false로 변경하여 활성 상태로 복원합니다.
     */
    public void restore() {
        this.deleted = false;
    }

    /**
     * 엔티티가 삭제된 상태인지 확인합니다.
     * @return 삭제된 상태이면 true, 활성 상태이면 false
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.deleted);
    }

    /**
     * 엔티티가 활성 상태인지 확인합니다.
     * @return 활성 상태이면 true, 삭제된 상태이면 false
     */
    public boolean isActive() {
        return !isDeleted();
    }
}
