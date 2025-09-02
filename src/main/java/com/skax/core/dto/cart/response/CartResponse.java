package com.skax.core.dto.cart.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 장바구니 응답 DTO
 * 
 * <p>장바구니 정보를 클라이언트에 전달할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장바구니 응답")
public class CartResponse {

    /**
     * 장바구니 번호
     */
    @Schema(description = "장바구니 번호", example = "1")
    private Long cno;

    /**
     * 회원 ID
     */
    @Schema(description = "회원 ID", example = "user123")
    private String memberId;

    /**
     * 장바구니 아이템 목록
     */
    @Schema(description = "장바구니 아이템 목록")
    private List<CartItemResponse> items;

    /**
     * 총 아이템 수
     */
    @Schema(description = "총 아이템 수", example = "5")
    private Integer totalItemCount;

    /**
     * 총 금액
     */
    @Schema(description = "총 금액", example = "2580000")
    private Integer totalAmount;

    /**
     * 생성 시간
     */
    @Schema(description = "생성 시간", example = "2025-08-23T10:30:00")
    private LocalDateTime createdAt;

    /**
     * 수정 시간
     */
    @Schema(description = "수정 시간", example = "2025-08-23T15:45:00")
    private LocalDateTime updatedAt;
}
