package com.skax.core.dto.cart.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장바구니 아이템 수정 요청 DTO
 * 
 * <p>장바구니 아이템의 수량을 수정할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장바구니 아이템 수정 요청")
public class CartItemUpdateRequest {

    /**
     * 수량
     */
    @NotNull(message = "수량은 필수입니다")
    @Positive(message = "수량은 1 이상이어야 합니다")
    @Schema(description = "변경할 상품 수량", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
}
