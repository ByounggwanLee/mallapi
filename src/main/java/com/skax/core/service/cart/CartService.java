package com.skax.core.service.cart;

import com.skax.core.dto.cart.request.CartItemAddRequest;
import com.skax.core.dto.cart.request.CartItemUpdateRequest;
import com.skax.core.dto.cart.response.CartResponse;
import com.skax.core.dto.cart.response.CartItemResponse;

import java.util.List;

/**
 * 장바구니 관리 서비스 인터페이스
 * 
 * <p>장바구니 및 장바구니 아이템의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 정의합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>장바구니 생성 및 조회</li>
 *   <li>장바구니 아이템 추가, 수정, 삭제</li>
 *   <li>장바구니 아이템 수량 조정</li>
 *   <li>장바구니 전체 비우기</li>
 *   <li>장바구니 총액 계산</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
public interface CartService {

    /**
     * 회원의 장바구니를 조회합니다. 장바구니가 없으면 새로 생성합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니 정보
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    CartResponse getOrCreateCart(String memberId);

    /**
     * 장바구니에 상품을 추가합니다.
     * 
     * @param memberId 회원 ID
     * @param request 장바구니 아이템 추가 요청
     * @return 추가된 장바구니 아이템 정보
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 상품인 경우
     */
    CartItemResponse addItemToCart(String memberId, CartItemAddRequest request);

    /**
     * 장바구니 아이템을 수정합니다 (수량 변경).
     * 
     * @param memberId 회원 ID
     * @param itemId 장바구니 아이템 ID
     * @param request 장바구니 아이템 수정 요청
     * @return 수정된 장바구니 아이템 정보
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 아이템인 경우
     */
    CartItemResponse updateCartItem(String memberId, Long itemId, CartItemUpdateRequest request);

    /**
     * 장바구니에서 특정 아이템을 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param itemId 삭제할 장바구니 아이템 ID
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 아이템인 경우
     */
    void removeItemFromCart(String memberId, Long itemId);

    /**
     * 장바구니의 모든 아이템을 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    void clearCart(String memberId);

    /**
     * 장바구니의 특정 아이템들을 일괄 삭제합니다.
     * 
     * @param memberId 회원 ID
     * @param itemIds 삭제할 장바구니 아이템 ID 목록
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    void removeItemsFromCart(String memberId, List<Long> itemIds);

    /**
     * 장바구니 아이템의 수량을 증가시킵니다.
     * 
     * @param memberId 회원 ID
     * @param itemId 장바구니 아이템 ID
     * @param quantity 증가시킬 수량
     * @return 수정된 장바구니 아이템 정보
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 아이템인 경우, 또는 수량이 음수인 경우
     */
    CartItemResponse increaseItemQuantity(String memberId, Long itemId, int quantity);

    /**
     * 장바구니 아이템의 수량을 감소시킵니다.
     * 
     * @param memberId 회원 ID
     * @param itemId 장바구니 아이템 ID
     * @param quantity 감소시킬 수량
     * @return 수정된 장바구니 아이템 정보
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 아이템인 경우, 또는 수량이 음수이거나 현재 수량보다 큰 경우
     */
    CartItemResponse decreaseItemQuantity(String memberId, Long itemId, int quantity);

    /**
     * 장바구니 총 아이템 수를 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니 총 아이템 수
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    int getCartItemCount(String memberId);

    /**
     * 장바구니 총 금액을 계산합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니 총 금액
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    int getCartTotalAmount(String memberId);

    /**
     * 장바구니가 비어있는지 확인합니다.
     * 
     * @param memberId 회원 ID
     * @return 장바구니가 비어있으면 true, 아니면 false
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    boolean isCartEmpty(String memberId);

    /**
     * 특정 상품이 장바구니에 있는지 확인합니다.
     * 
     * @param memberId 회원 ID
     * @param productId 상품 ID
     * @return 상품이 장바구니에 있으면 true, 아니면 false
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    boolean hasProductInCart(String memberId, Long productId);

    /**
     * 장바구니에서 특정 상품의 수량을 조회합니다.
     * 
     * @param memberId 회원 ID
     * @param productId 상품 ID
     * @return 상품 수량 (장바구니에 없으면 0)
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    int getProductQuantityInCart(String memberId, Long productId);
}
