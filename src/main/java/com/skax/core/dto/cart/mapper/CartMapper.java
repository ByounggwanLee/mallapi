package com.skax.core.dto.cart.mapper;

import com.skax.core.dto.cart.request.CartItemAddRequest;
import com.skax.core.dto.cart.request.CartItemUpdateRequest;
import com.skax.core.dto.cart.response.CartResponse;
import com.skax.core.dto.cart.response.CartItemResponse;
import com.skax.core.entity.cart.Cart;
import com.skax.core.entity.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Cart/CartItem 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CartMapper {

    /**
     * Cart 엔티티를 CartResponse DTO로 변환합니다.
     * 
     * @param cart Cart 엔티티
     * @return CartResponse DTO
     */
    @Mapping(target = "cno", source = "cno")
    @Mapping(target = "memberId", source = "owner.email")
    @Mapping(target = "items", ignore = true)  // 별도로 처리
    @Mapping(target = "totalItemCount", ignore = true)  // 별도로 계산
    @Mapping(target = "totalAmount", ignore = true)  // 별도로 계산
    CartResponse toResponse(Cart cart);

    /**
     * CartItem 엔티티를 CartItemResponse DTO로 변환합니다.
     * 
     * @param cartItem CartItem 엔티티
     * @return CartItemResponse DTO
     */
    @Mapping(target = "itemId", source = "cino")
    @Mapping(target = "cartId", source = "cart.cno")
    @Mapping(target = "productId", source = "product.pno")
    @Mapping(target = "productName", source = "product.pname")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "productImage", ignore = true)
    @Mapping(target = "quantity", source = "qty")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getQty() * cartItem.getProduct().getPrice())")
    CartItemResponse toItemResponse(CartItem cartItem);

    /**
     * CartItemAddRequest DTO를 CartItem 엔티티로 변환합니다.
     * 
     * @param request CartItemAddRequest DTO
     * @return CartItem 엔티티
     */
    @Mapping(target = "cino", ignore = true)
    @Mapping(target = "product", ignore = true)  // 별도로 설정
    @Mapping(target = "cart", ignore = true)     // 별도로 설정
    @Mapping(target = "qty", source = "quantity")
    CartItem toEntity(CartItemAddRequest request);

    /**
     * CartItemUpdateRequest DTO를 사용하여 CartItem 엔티티를 업데이트합니다.
     * 
     * @param request CartItemUpdateRequest DTO
     * @param cartItem 수정할 CartItem 엔티티
     */
    default void updateFromRequest(CartItemUpdateRequest request, @MappingTarget CartItem cartItem) {
        if (request.getQuantity() != null) {
            cartItem.changeQty(request.getQuantity());
        }
    }
}
