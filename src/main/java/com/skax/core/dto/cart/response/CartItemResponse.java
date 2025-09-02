package com.skax.core.dto.cart.response;

import com.skax.core.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 장바구니 아이템 응답 DTO
 * 
 * <p>장바구니 아이템 정보를 클라이언트에 전달할 때 사용하는 데이터 전송 객체입니다.</p>
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
@Schema(description = "장바구니 아이템 응답")
public class CartItemResponse extends BaseDto {

    /**
     * 장바구니 아이템 번호
     */
    @Schema(description = "장바구니 아이템 번호", example = "1")
    private Long itemId;

    /**
     * 장바구니 번호
     */
    @Schema(description = "장바구니 번호", example = "1")
    private Long cartId;

    /**
     * 상품 번호
     */
    @Schema(description = "상품 번호", example = "1")
    private Long productId;

    /**
     * 상품명
     */
    @Schema(description = "상품명", example = "iPhone 15 Pro")
    private String productName;

    /**
     * 상품 가격
     */
    @Schema(description = "상품 가격", example = "1290000")
    private Integer productPrice;

    /**
     * 상품 이미지 (대표 이미지)
     */
    @Schema(description = "상품 이미지", example = "iphone15pro.jpg")
    private String productImage;

    /**
     * 수량
     */
    @Schema(description = "수량", example = "2")
    private Integer quantity;

    /**
     * 총 가격 (상품 가격 * 수량)
     */
    @Schema(description = "총 가격", example = "2580000")
    private Integer totalPrice;
}
