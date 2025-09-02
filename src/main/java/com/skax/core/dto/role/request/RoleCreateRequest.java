package com.skax.core.dto.role.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 역할 생성 요청 DTO
 * 
 * <p>새로운 역할을 등록할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "역할 생성 요청")
public class RoleCreateRequest {

    /**
     * 역할명
     */
    @NotBlank(message = "역할명은 필수입니다")
    @Size(min = 2, max = 50, message = "역할명은 2자 이상 50자 이하로 입력해주세요")
    @Schema(description = "역할명", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    /**
     * 역할 설명
     */
    @Size(max = 200, message = "역할 설명은 200자 이하로 입력해주세요")
    @Schema(description = "역할 설명", example = "관리자 권한")
    private String description;

    /**
     * 기본 역할 여부
     */
    @Schema(description = "기본 역할 여부", example = "false")
    private Boolean isDefault;
}
