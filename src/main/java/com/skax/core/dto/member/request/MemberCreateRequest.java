package com.skax.core.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 생성 요청 DTO
 * 
 * <p>새로운 회원 가입 시 클라이언트로부터 받는 데이터를 담는 클래스입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 생성 요청 데이터")
public class MemberCreateRequest {

    /**
     * 회원 이메일
     */
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Schema(description = "회원 이메일", example = "user@example.com", required = true)
    private String email;

    /**
     * 회원 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다")
    @Schema(description = "회원 비밀번호", example = "password123!", required = true, minLength = 8, maxLength = 100)
    private String password;

    /**
     * 회원 닉네임
     */
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다")
    @Schema(description = "회원 닉네임", example = "개발자김씨", required = true, minLength = 2, maxLength = 50)
    private String nickname;

    /**
     * 소셜 로그인 여부
     */
    @Builder.Default
    @Schema(description = "소셜 로그인 여부", example = "false")
    private boolean social = false;
}
