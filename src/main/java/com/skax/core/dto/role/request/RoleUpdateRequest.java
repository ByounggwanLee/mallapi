package com.skax.core.dto.role.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 역할 수정 요청 DTO
 * 
 * <p>기존 역할 정보를 수정할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "역할 수정 요청")
public class RoleUpdateRequest {

    /**
     * 역할명
     */
    @Size(min = 2, max = 50, message = "역할명은 2자 이상 50자 이하로 입력해주세요")
    @Schema(description = "역할명", example = "SUPER_ADMIN")
    private String roleName;

    /**
     * 역할 설명
     */
    @Size(max = 200, message = "역할 설명은 200자 이하로 입력해주세요")
    @Schema(description = "역할 설명", example = "최고 관리자 권한")
    private String description;

    /**
     * 기본 역할 여부
     */
    @Schema(description = "기본 역할 여부", example = "false")
    private Boolean isDefault;
}
