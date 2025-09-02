package com.skax.core.dto.todo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 할일 생성 요청 DTO
 * 
 * <p>새로운 할일 생성 시 클라이언트로부터 받는 데이터를 담는 클래스입니다.</p>
 * 
 * <p>포함되는 정보:</p>
 * <ul>
 *   <li>할일 제목 (필수)</li>
 *   <li>작성자 (필수)</li>
 *   <li>완료 여부 (기본값: false)</li>
 *   <li>마감일 (필수)</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "할일 생성 요청 데이터")
public class TodoCreateRequest {

    /**
     * 할일 제목
     * 최대 256자까지 입력 가능합니다.
     */
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 256, message = "제목은 256자 이하여야 합니다.")
    @Schema(description = "할일 제목", example = "Spring Boot 프로젝트 완성하기", required = true, maxLength = 256)
    private String title;

    /**
     * 할일 작성자
     * 최대 256자까지 입력 가능합니다.
     */
    @NotBlank(message = "작성자는 필수입니다.")
    @Size(max = 256, message = "작성자는 256자 이하여야 합니다.")
    @Schema(description = "할일 작성자", example = "김개발", required = true, maxLength = 256)
    private String writer;

    /**
     * 할일 완료 여부
     * 기본값은 false(미완료)입니다.
     */
    @Builder.Default
    @Schema(description = "완료 여부", example = "false")
    private Boolean complete = false;
}
