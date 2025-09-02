package com.skax.core.service.cart.impl;

import com.skax.core.dto.cart.request.CartItemAddRequest;
import com.skax.core.dto.cart.request.CartItemUpdateRequest;
import com.skax.core.dto.cart.response.CartResponse;
import com.skax.core.dto.cart.response.CartItemResponse;
import com.skax.core.entity.cart.Cart;
import com.skax.core.entity.cart.CartItem;
import com.skax.core.entity.member.Member;
import com.skax.core.entity.product.Product;
import com.skax.core.dto.cart.mapper.CartMapper;
import com.skax.core.repository.cart.CartRepository;
import com.skax.core.repository.cart.CartItemRepository;
import com.skax.core.repository.member.MemberRepository;
import com.skax.core.repository.product.ProductRepository;
import com.skax.core.service.cart.CartService;
import com.skax.core.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 장바구니 관리 서비스 구현체
 * 
 * <p>장바구니 및 장바구니 아이템의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 처리합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final ServiceUtils serviceUtils;

    @Override
    @Transactional
    public CartResponse getOrCreateCart(String memberId) {
        log.debug("Getting or creating cart for member: {}", memberId);
        
        // 회원 존재 확인
        Member member = memberRepository.findByEmail(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));
        
        // 기존 장바구니 조회 또는 생성
        Cart cart = cartRepository.findByOwnerEmail(memberId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .owner(member)
                            .build();
                    return cartRepository.save(newCart);
                });
        
        CartResponse response = cartMapper.toResponse(cart);
        serviceUtils.mapWithAudit(cart, response);
        
        // 장바구니 아이템들 조회 및 설정
        List<CartItem> items = cartItemRepository.findByCartId(cart.getCno());
        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> {
                    CartItemResponse itemResponse = cartMapper.toItemResponse(item);
                    return serviceUtils.mapWithAudit(item, itemResponse);
                })
                .toList();
        
        // items 필드 수동 설정
        response.setItems(itemResponses);
        
        return response;
    }

    @Override
    @Transactional
    public CartItemResponse addItemToCart(String memberId, CartItemAddRequest request) {
        log.info("Adding item to cart - member: {}, product: {}, quantity: {}", 
                memberId, request.getProductId(), request.getQuantity());
        
        // 장바구니 조회 또는 생성
        CartResponse cartResponse = getOrCreateCart(memberId);
        Cart cart = cartRepository.findById(cartResponse.getCno())
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다"));
        
        // 상품 존재 확인
        Product product = productRepository.findByPnoAndDelFlagFalse(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + request.getProductId()));
        
        // 기존 장바구니 아이템 확인
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(
                cart.getCno(), request.getProductId()).orElse(null);
        
        if (existingItem != null) {
            // 기존 아이템의 수량 증가
            existingItem.changeQty(existingItem.getQty() + request.getQuantity());
            CartItem updatedItem = cartItemRepository.save(existingItem);
            log.info("Updated existing cart item with id: {}", updatedItem.getCino());
            CartItemResponse response = cartMapper.toItemResponse(updatedItem);
            return serviceUtils.mapWithAudit(updatedItem, response);
        } else {
            // 새로운 아이템 추가
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .qty(request.getQuantity())
                    .build();
            
            CartItem savedItem = cartItemRepository.save(cartItem);
            log.info("Successfully added new cart item with id: {}", savedItem.getCino());
            CartItemResponse response = cartMapper.toItemResponse(savedItem);
            return serviceUtils.mapWithAudit(savedItem, response);
        }
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(String memberId, Long itemId, CartItemUpdateRequest request) {
        log.info("Updating cart item with id: {} for member: {}", itemId, memberId);
        
        // 장바구니 아이템 조회 및 소유자 확인
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 아이템입니다: " + itemId));
        
        if (!cartItem.getCart().getOwner().getEmail().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 아이템에 접근할 권한이 없습니다");
        }
        
        // 수량 업데이트
        cartMapper.updateFromRequest(request, cartItem);
        
        CartItem updatedItem = cartItemRepository.save(cartItem);
        log.info("Successfully updated cart item with id: {}", itemId);
        
        CartItemResponse response = cartMapper.toItemResponse(updatedItem);
        return serviceUtils.mapWithAudit(updatedItem, response);
    }

    @Override
    @Transactional
    public void removeItemFromCart(String memberId, Long itemId) {
        log.info("Removing cart item with id: {} for member: {}", itemId, memberId);
        
        // 장바구니 아이템 조회 및 소유자 확인
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 아이템입니다: " + itemId));
        
        if (!cartItem.getCart().getOwner().getEmail().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 아이템에 접근할 권한이 없습니다");
        }
        
        cartItemRepository.delete(cartItem);
        log.info("Successfully removed cart item with id: {}", itemId);
    }

    @Override
    @Transactional
    public void removeItemsFromCart(String memberId, List<Long> itemIds) {
        log.info("Removing {} cart items for member: {}", itemIds.size(), memberId);
        
        for (Long itemId : itemIds) {
            removeItemFromCart(memberId, itemId);
        }
        
        log.info("Successfully removed {} cart items for member: {}", itemIds.size(), memberId);
    }

    @Override
    @Transactional
    public void clearCart(String memberId) {
        log.info("Clearing cart for member: {}", memberId);
        
        // 장바구니 조회
        Cart cart = cartRepository.findByOwnerEmail(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다"));
        
        cartItemRepository.deleteByCartId(cart.getCno());
        
        log.info("Successfully cleared cart for member: {}", memberId);
    }

    @Override
    @Transactional
    public CartItemResponse increaseItemQuantity(String memberId, Long itemId, int amount) {
        log.info("Increasing quantity for cart item with id: {} by {} for member: {}", itemId, amount, memberId);
        
        // 장바구니 아이템 조회 및 소유자 확인
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 아이템입니다: " + itemId));
        
        if (!cartItem.getCart().getOwner().getEmail().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 아이템에 접근할 권한이 없습니다");
        }
        
        cartItem.changeQty(cartItem.getQty() + amount);
        CartItem updatedItem = cartItemRepository.save(cartItem);
        
        log.info("Successfully increased quantity for cart item with id: {}", itemId);
        
        CartItemResponse response = cartMapper.toItemResponse(updatedItem);
        return serviceUtils.mapWithAudit(updatedItem, response);
    }

    @Override
    @Transactional
    public CartItemResponse decreaseItemQuantity(String memberId, Long itemId, int amount) {
        log.info("Decreasing quantity for cart item with id: {} by {} for member: {}", itemId, amount, memberId);
        
        // 장바구니 아이템 조회 및 소유자 확인
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 아이템입니다: " + itemId));
        
        if (!cartItem.getCart().getOwner().getEmail().equals(memberId)) {
            throw new IllegalArgumentException("해당 장바구니 아이템에 접근할 권한이 없습니다");
        }
        
        int newQuantity = cartItem.getQty() - amount;
        if (newQuantity <= 0) {
            // 수량이 0 이하가 되면 아이템 삭제
            cartItemRepository.delete(cartItem);
            log.info("Removed cart item with id: {} as quantity became 0 or less", itemId);
            return null; // 또는 삭제된 아이템 정보를 반환
        } else {
            cartItem.changeQty(newQuantity);
            CartItem updatedItem = cartItemRepository.save(cartItem);
            log.info("Successfully decreased quantity for cart item with id: {}", itemId);
            CartItemResponse response = cartMapper.toItemResponse(updatedItem);
            return serviceUtils.mapWithAudit(updatedItem, response);
        }
    }

    @Override
    public boolean hasProductInCart(String memberId, Long productId) {
        log.debug("Checking if product {} is in cart for member: {}", productId, memberId);
        
        // 장바구니 조회
        Cart cart = cartRepository.findByOwnerEmail(memberId).orElse(null);
        if (cart == null) {
            return false;
        }
        
        return cartItemRepository.findByCartIdAndProductId(cart.getCno(), productId).isPresent();
    }

    @Override
    public int getProductQuantityInCart(String memberId, Long productId) {
        log.debug("Getting quantity of product {} in cart for member: {}", productId, memberId);
        
        // 장바구니 조회
        Cart cart = cartRepository.findByOwnerEmail(memberId).orElse(null);
        if (cart == null) {
            return 0;
        }
        
        return cartItemRepository.findByCartIdAndProductId(cart.getCno(), productId)
                .map(CartItem::getQty)
                .orElse(0);
    }

    @Override
    public int getCartItemCount(String memberId) {
        log.debug("Getting cart item count for member: {}", memberId);
        
        // 장바구니 조회
        Cart cart = cartRepository.findByOwnerEmail(memberId).orElse(null);
        if (cart == null) {
            return 0;
        }
        
        return (int) cartItemRepository.countByCartId(cart.getCno());
    }

    @Override
    public boolean isCartEmpty(String memberId) {
        log.debug("Checking if cart is empty for member: {}", memberId);
        
        return getCartItemCount(memberId) == 0;
    }

    @Override
    public int getCartTotalAmount(String memberId) {
        log.debug("Getting total amount for member: {}", memberId);
        
        // 장바구니 조회
        Cart cart = cartRepository.findByOwnerEmail(memberId).orElse(null);
        if (cart == null) {
            return 0;
        }
        
        List<CartItem> items = cartItemRepository.findByCartId(cart.getCno());
        return items.stream()
                .mapToInt(item -> item.getQty() * item.getProduct().getPrice())
                .sum();
    }
}
