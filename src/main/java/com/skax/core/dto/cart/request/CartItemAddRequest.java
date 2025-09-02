package com.skax.core.dto.cart.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장바구니 아이템 추가 요청 DTO
 * 
 * <p>장바구니에 새로운 상품을 추가할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장바구니 아이템 추가 요청")
public class CartItemAddRequest {

    /**
     * 상품 번호
     */
    @NotNull(message = "상품 번호는 필수입니다")
    @Schema(description = "상품 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    /**
     * 수량
     */
    @NotNull(message = "수량은 필수입니다")
    @Positive(message = "수량은 1 이상이어야 합니다")
    @Schema(description = "상품 수량", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
}
