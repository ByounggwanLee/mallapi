package com.skax.core.dto.member.response;

import com.skax.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * 회원 정보 응답 DTO
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse extends BaseDto {

    /**
     * 회원 ID
     */
    private Long id;

    /**
     * 이메일
     */
    private String email;

    /**
     * 이름
     */
    private String name;

    /**
     * 프로필 이미지 URL
     */
    private String pictureUrl;

    /**
     * OAuth2 제공자
     */
    private String provider;

    /**
     * 활성화 상태
     */
    private Boolean isActive;

    /**
     * 역할 목록
     */
    private Set<String> roles;
}
