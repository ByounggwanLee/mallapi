package com.skax.core.dto.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 상품 생성 요청 DTO
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
public class ProductCreateRequest {

    /**
     * 상품명
     */
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 200, message = "상품명은 200자 이하여야 합니다.")
    private String productName;

    /**
     * 설명
     */
    @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
    private String description;

    /**
     * 가격
     */
    @NotNull(message = "가격은 필수입니다.")
    @DecimalMin(value = "0.0", message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;

    /**
     * 카테고리
     */
    @Size(max = 100, message = "카테고리는 100자 이하여야 합니다.")
    private String category;

    /**
     * 재고 수량
     */
    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stockQuantity;

    /**
     * 이미지 URL
     */
    @Size(max = 500, message = "이미지 URL은 500자 이하여야 합니다.")
    private String imageUrl;
}
