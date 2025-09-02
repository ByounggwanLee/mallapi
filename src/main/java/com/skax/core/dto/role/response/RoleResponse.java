package com.skax.core.dto.role.response;

import com.skax.core.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 역할 응답 DTO
 * 
 * <p>역할 정보를 클라이언트에 전달할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "역할 응답")
public class RoleResponse extends BaseDto {

    /**
     * 역할 ID
     */
    @Schema(description = "역할 ID", example = "1")
    private Long id;

    /**
     * 역할명
     */
    @Schema(description = "역할명", example = "ADMIN")
    private String roleName;

    /**
     * 역할 설명
     */
    @Schema(description = "역할 설명", example = "관리자 권한")
    private String description;

    /**
     * 기본 역할 여부
     */
    @Schema(description = "기본 역할 여부", example = "false")
    private Boolean isDefault;

    /**
     * 해당 역할을 가진 회원 수
     */
    @Schema(description = "해당 역할을 가진 회원 수", example = "5")
    private Long memberCount;
}
