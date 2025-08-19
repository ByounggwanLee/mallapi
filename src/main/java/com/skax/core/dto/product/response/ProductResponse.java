package com.skax.core.dto.product.response;

import com.skax.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 상품 정보 응답 DTO
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
public class ProductResponse extends BaseDto {

    /**
     * 상품 ID
     */
    private Long id;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 설명
     */
    private String description;

    /**
     * 가격
     */
    private BigDecimal price;

    /**
     * 카테고리
     */
    private String category;

    /**
     * 재고 수량
     */
    private Integer stockQuantity;

    /**
     * 이미지 URL
     */
    private String imageUrl;

    /**
     * 판매 가능 여부
     */
    private Boolean isAvailable;

    /**
     * 상품 상태
     */
    private String status;

    /**
     * 등록자 ID
     */
    private Long memberId;

    /**
     * 등록자 이름
     */
    private String memberName;
}
