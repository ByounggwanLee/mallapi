package com.skax.core.entity.member;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 회원 정보를 나타내는 엔티티 클래스
 * 
 * <p>시스템 사용자의 기본 정보와 OAuth2 소셜 로그인 정보를 관리합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>이메일 주소 (기본키)</li>
 *   <li>비밀번호</li>
 *   <li>닉네임</li>
 *   <li>소셜 로그인 여부</li>
 *   <li>OAuth2 제공자 정보 (Google, Naver, Kakao 등)</li>
 *   <li>회원 권한 목록</li>
 * </ul>
 * 
 * <p>이메일을 기본키로 사용하며, 소셜 로그인과 일반 로그인을 모두 지원합니다.</p>
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
@ToString(exclude = "memberRoleList")
public class Member {

    /**
     * 회원 이메일 주소 (기본키)
     * 시스템 내에서 회원을 고유하게 식별하는 이메일 주소입니다.
     */
    @Id
    private String email;

    /**
     * 회원 비밀번호
     * 일반 로그인 시 사용되는 암호화된 비밀번호입니다.
     * 소셜 로그인 사용자의 경우 null일 수 있습니다.
     */
    private String pw;

    /**
     * 회원 닉네임
     * 시스템에서 표시되는 사용자의 별칭입니다.
     */
    private String nickname;

    /**
     * 소셜 로그인 여부
     * true: 소셜 로그인 사용자, false: 일반 로그인 사용자
     */
    private boolean social;

    /**
     * OAuth2 제공자 (Google, Naver, Kakao 등)
     * 소셜 로그인을 제공하는 서비스의 이름입니다.
     * 일반 로그인 사용자의 경우 null입니다.
     */
    private String provider;

    /**
     * OAuth2 제공자에서 제공하는 고유 식별자
     * 각 소셜 로그인 서비스에서 사용자를 식별하는 고유 ID입니다.
     * 일반 로그인 사용자의 경우 null입니다.
     */
    private String providerId;

    /**
     * 회원 활성화 상태
     * true: 활성화된 회원, false: 비활성화된 회원
     * 비활성화된 회원은 로그인 및 서비스 이용이 제한됩니다.
     */
    @Builder.Default
    private Boolean isActive = true;

    /**
     * 회원 권한 목록
     * 회원이 가지고 있는 권한들의 목록입니다.
     * 지연 로딩으로 설정되어 필요할 때만 조회됩니다.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    /**
     * 회원에게 새로운 권한을 추가합니다.
     * 
     * @param memberRole 추가할 회원 권한
     * @throws IllegalArgumentException memberRole이 null인 경우
     */
    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    /**
     * 회원의 모든 권한을 제거합니다.
     * 권한 목록을 초기화하여 빈 상태로 만듭니다.
     */
    public void clearRole() {
        memberRoleList.clear();
    }

    /**
     * 회원의 닉네임을 변경합니다.
     * 
     * @param nickname 새로운 닉네임
     * @throws IllegalArgumentException nickname이 null이거나 빈 문자열인 경우
     */
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 회원의 비밀번호를 변경합니다.
     * 
     * @param pw 새로운 비밀번호 (암호화된 상태)
     * @throws IllegalArgumentException pw가 null이거나 빈 문자열인 경우
     */
    public void changePw(String pw) {
        this.pw = pw;
    }

    /**
     * 회원의 소셜 로그인 여부를 변경합니다.
     * 
     * @param social 소셜 로그인 여부 (true: 소셜 로그인, false: 일반 로그인)
     */
    public void changeSocial(boolean social) {
        this.social = social;
    }

    /**
     * OAuth2 제공자 정보를 설정합니다.
     * 
     * @param provider OAuth2 제공자 (Google, Naver, Kakao 등)
     * @param providerId OAuth2 제공자에서 제공하는 고유 식별자
     */
    public void setOAuth2Info(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
        this.social = true;
    }

    /**
     * OAuth2 제공자 정보를 초기화합니다.
     * 소셜 로그인에서 일반 로그인으로 변경할 때 사용합니다.
     */
    public void clearOAuth2Info() {
        this.provider = null;
        this.providerId = null;
        this.social = false;
    }

    /**
     * 회원을 활성화합니다.
     * 비활성화된 회원의 서비스 이용을 재개할 때 사용합니다.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * 회원을 비활성화합니다.
     * 회원의 서비스 이용을 일시적으로 제한할 때 사용합니다.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 회원의 활성화 상태를 변경합니다.
     * 
     * @param isActive 활성화 상태 (true: 활성화, false: 비활성화)
     */
    public void changeActiveStatus(Boolean isActive) {
        this.isActive = isActive;
    }

}
