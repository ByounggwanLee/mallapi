package com.skax.core.controller.cart;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.dto.cart.request.CartItemAddRequest;
import com.skax.core.dto.cart.request.CartItemUpdateRequest;
import com.skax.core.dto.cart.response.CartResponse;
import com.skax.core.dto.cart.response.CartItemResponse;
import com.skax.core.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 장바구니 관리 컨트롤러
 * 
 * <p>장바구니 및 장바구니 아이템의 생성, 조회, 수정, 삭제 등의 REST API를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Carts", description = "장바구니 관리 API")
public class CartController {

    private final CartService cartService;

    /**
     * 회원의 장바구니를 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니 정보
     */
    @Operation(summary = "장바구니 조회", description = "회원의 장바구니를 조회합니다. 없으면 새로 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @GetMapping("/{memberId}")
    public AxResponseEntity<CartResponse> getCart(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId) {
        log.info("장바구니 조회 - 회원 ID: {}", memberId);
        
        CartResponse cart = cartService.getOrCreateCart(memberId);
        return AxResponseEntity.ok(cart, "장바구니를 성공적으로 조회했습니다.");
    }

    /**
     * 장바구니에 상품을 추가합니다.
     * 
     * @param memberId 회원 ID
     * @param request 장바구니 아이템 추가 요청 데이터
     * @return 추가된 장바구니 아이템 정보
     */
    @Operation(summary = "장바구니 아이템 추가", description = "장바구니에 상품을 추가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "장바구니 아이템 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "회원 또는 상품을 찾을 수 없음")
    })
    @PostMapping("/{memberId}/items")
    public AxResponseEntity<CartItemResponse> addItemToCart(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId,
            @Valid @RequestBody CartItemAddRequest request) {
        log.info("장바구니 아이템 추가 - 회원 ID: {}, 상품 ID: {}", memberId, request.getProductId());
        
        CartItemResponse cartItem = cartService.addItemToCart(memberId, request);
        return AxResponseEntity.created(cartItem, "장바구니에 상품이 성공적으로 추가되었습니다.");
    }

    /**
     * 장바구니 아이템을 수정합니다.
     * 
     * @param memberId 회원 ID
     * @param itemId 장바구니 아이템 ID
     * @param request 장바구니 아이템 수정 요청 데이터
     * @return 수정된 장바구니 아이템 정보
     */
    @Operation(summary = "장바구니 아이템 수정", description = "장바구니 아이템의 수량을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 아이템 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "회원 또는 아이템을 찾을 수 없음")
    })
    @PutMapping("/{memberId}/items/{itemId}")
    public AxResponseEntity<CartItemResponse> updateCartItem(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId,
            @Parameter(description = "장바구니 아이템 ID", example = "1")
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        log.info("장바구니 아이템 수정 - 회원 ID: {}, 아이템 ID: {}", memberId, itemId);
        
        CartItemResponse cartItem = cartService.updateCartItem(memberId, itemId, request);
        return AxResponseEntity.updated(cartItem, "장바구니 아이템이 성공적으로 수정되었습니다.");
    }

    /**
     * 장바구니에서 특정 아이템을 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param itemId 장바구니 아이템 ID
     */
    @Operation(summary = "장바구니 아이템 삭제", description = "장바구니에서 특정 아이템을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 아이템 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회원 또는 아이템을 찾을 수 없음")
    })
    @DeleteMapping("/{memberId}/items/{itemId}")
    public AxResponseEntity<Void> removeItemFromCart(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId,
            @Parameter(description = "장바구니 아이템 ID", example = "1")
            @PathVariable Long itemId) {
        log.info("장바구니 아이템 삭제 - 회원 ID: {}, 아이템 ID: {}", memberId, itemId);
        
        cartService.removeItemFromCart(memberId, itemId);
        return AxResponseEntity.deleted("장바구니 아이템이 성공적으로 삭제되었습니다.");
    }

    /**
     * 장바구니의 모든 아이템을 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 아이템을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 비우기 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @DeleteMapping("/{memberId}/items")
    public AxResponseEntity<Void> clearCart(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId) {
        log.info("장바구니 비우기 - 회원 ID: {}", memberId);
        
        cartService.clearCart(memberId);
        return AxResponseEntity.deleted("장바구니가 성공적으로 비워졌습니다.");
    }

    /**
     * 장바구니의 특정 아이템들을 일괄 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param itemIds 삭제할 장바구니 아이템 ID 목록
     */
    @Operation(summary = "장바구니 아이템 일괄 삭제", description = "장바구니의 특정 아이템들을 일괄 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 아이템 일괄 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @DeleteMapping("/{memberId}/items/batch")
    public AxResponseEntity<Void> removeItemsFromCart(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId,
            @Parameter(description = "삭제할 장바구니 아이템 ID 목록")
            @RequestBody List<Long> itemIds) {
        log.info("장바구니 아이템 일괄 삭제 - 회원 ID: {}, 아이템 수: {}", memberId, itemIds.size());
        
        cartService.removeItemsFromCart(memberId, itemIds);
        return AxResponseEntity.deleted("선택된 장바구니 아이템들이 성공적으로 삭제되었습니다.");
    }

    /**
     * 장바구니 총 금액을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니 총 금액
     */
    @Operation(summary = "장바구니 총 금액 조회", description = "장바구니 총 금액을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장바구니 총 금액 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @GetMapping("/{memberId}/total")
    public AxResponseEntity<Integer> getCartTotalAmount(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId) {
        log.info("장바구니 총 금액 조회 - 회원 ID: {}", memberId);
        
        int totalAmount = cartService.getCartTotalAmount(memberId);
        return AxResponseEntity.ok(totalAmount, "장바구니 총 금액을 성공적으로 조회했습니다.");
    }
}
