package com.skax.core.entity.member;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 회원과 역할 간의 다대다 관계를 나타내는 엔티티 클래스
 * 
 * <p>회원이 가진 역할들을 저장하는 중간 테이블입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "member_roles", 
       uniqueConstraints = @UniqueConstraint(name = "uk_member_role", columnNames = {"member_id", "role_id"}),
       indexes = {
           @Index(name = "idx_member_role_member", columnList = "member_id"),
           @Index(name = "idx_member_role_role", columnList = "role_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRole extends BaseEntity {

    /**
     * 회원-역할 연결 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    /**
     * 회원 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 역할 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * 역할 활성화 상태
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRole)) return false;
        MemberRole that = (MemberRole) o;
        return member != null && member.equals(that.member) &&
               role != null && role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
