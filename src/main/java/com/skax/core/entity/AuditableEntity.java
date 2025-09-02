    package com.skax.core.entity;

import com.skax.core.entity.member.Member;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * 작성자, 수정자 정보를 관리하는 감사 베이스 클래스
 * BaseTimeEntity -> AuditableEntity 상속 구조
 * Spring Data JPA Auditing을 사용하여 자동으로 생성자/수정자를 설정합니다.
 * @author ByounggwanLee
 */
@Getter
@Setter
public abstract class AuditableEntity extends BaseTimeEntity {
    
    /**
     * 엔티티 생성자
     * 엔티티가 처음 저장될 때 자동으로 설정됩니다.
     */
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;

    /**
     * 엔티티 최종 수정자
     * 엔티티가 수정될 때마다 자동으로 갱신됩니다.
     */
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;
}
