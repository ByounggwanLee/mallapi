package com.skax.core.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 수정 요청 DTO
 * 
 * <p>기존 상품 정보를 수정할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 수정 요청")
public class ProductUpdateRequest {

    /**
     * 상품명
     */
    @Size(min = 1, max = 100, message = "상품명은 1자 이상 100자 이하로 입력해주세요")
    @Schema(description = "상품명", example = "iPhone 15 Pro Max")
    private String productName;

    /**
     * 상품 설명
     */
    @Size(max = 1000, message = "상품 설명은 1000자 이하로 입력해주세요")
    @Schema(description = "상품 설명", example = "업데이트된 상품 설명")
    private String description;

    /**
     * 상품 가격
     */
    @PositiveOrZero(message = "상품 가격은 0 이상이어야 합니다")
    @Schema(description = "상품 가격", example = "1490000")
    private Integer price;

    /**
     * 상품 이미지 목록
     */
    @Schema(description = "상품 이미지 파일명 목록", example = "[\"new_image1.jpg\", \"new_image2.jpg\"]")
    private List<String> images;
}
