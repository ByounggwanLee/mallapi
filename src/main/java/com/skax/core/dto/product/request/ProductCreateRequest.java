package com.skax.core.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 생성 요청 DTO
 * 
 * <p>새로운 상품을 등록할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 생성 요청")
public class ProductCreateRequest {

    /**
     * 상품명
     */
    @NotBlank(message = "상품명은 필수입니다")
    @Size(min = 1, max = 100, message = "상품명은 1자 이상 100자 이하로 입력해주세요")
    @Schema(description = "상품명", example = "iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productName;

    /**
     * 상품 설명
     */
    @Size(max = 1000, message = "상품 설명은 1000자 이하로 입력해주세요")
    @Schema(description = "상품 설명", example = "최신 iPhone 15 Pro 모델")
    private String description;

    /**
     * 상품 가격
     */
    @NotNull(message = "상품 가격은 필수입니다")
    @PositiveOrZero(message = "상품 가격은 0 이상이어야 합니다")
    @Schema(description = "상품 가격", example = "1290000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer price;

    /**
     * 상품 이미지 목록
     */
    @Schema(description = "상품 이미지 파일명 목록", example = "[\"image1.jpg\", \"image2.jpg\"]")
    private List<String> images;
}
