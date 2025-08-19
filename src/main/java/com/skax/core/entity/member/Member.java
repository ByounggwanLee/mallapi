package com.skax.core.entity.member;

import com.skax.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 회원 정보를 나타내는 엔티티 클래스
 * 
 * <p>회원의 기본 정보와 OAuth2 로그인 정보를 저장합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "members", indexes = {
    @Index(name = "idx_member_email", columnList = "email"),
    @Index(name = "idx_member_provider_id", columnList = "provider, provider_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    /**
     * 회원 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 회원 이메일 (고유값)
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 회원 이름
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 회원 프로필 이미지 URL
     */
    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    /**
     * OAuth2 제공자 (google, github 등)
     */
    @Column(name = "provider", length = 20)
    private String provider;

    /**
     * OAuth2 제공자에서의 사용자 ID
     */
    @Column(name = "provider_id", length = 100)
    private String providerId;

    /**
     * 회원 활성화 상태
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * 회원이 가진 역할들
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> memberRoles = new HashSet<>();

    /**
     * 회원에게 역할을 추가합니다.
     * 
     * @param role 추가할 역할
     */
    public void addRole(Role role) {
        MemberRole memberRole = MemberRole.builder()
            .member(this)
            .role(role)
            .build();
        this.memberRoles.add(memberRole);
    }

    /**
     * 회원에서 역할을 제거합니다.
     * 
     * @param role 제거할 역할
     */
    public void removeRole(Role role) {
        this.memberRoles.removeIf(memberRole -> memberRole.getRole().equals(role));
    }

    /**
     * 회원 정보를 업데이트합니다.
     * 
     * @param name 새로운 이름
     * @param pictureUrl 새로운 프로필 이미지 URL
     */
    public void updateMemberInfo(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
    }
}
