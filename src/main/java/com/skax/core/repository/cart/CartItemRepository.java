package com.skax.core.repository.cart;

import com.skax.core.entity.cart.Cart;
import com.skax.core.entity.cart.CartItem;
import com.skax.core.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CartItem 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>장바구니 아이템 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>장바구니별 아이템 조회</li>
 *   <li>상품별 장바구니 아이템 조회</li>
 *   <li>장바구니와 상품 조합 검색</li>
 *   <li>수량 업데이트</li>
 *   <li>일괄 삭제 작업</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 특정 장바구니의 모든 아이템을 조회합니다.
     * 
     * @param cart 장바구니
     * @return 해당 장바구니의 아이템 목록
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * 장바구니 ID로 모든 아이템을 조회합니다.
     * 
     * @param cartId 장바구니 ID
     * @return 해당 장바구니의 아이템 목록
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cno = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    /**
     * 회원 이메일로 장바구니 아이템들을 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 해당 회원의 장바구니 아이템 목록
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.owner.email = :email")
    List<CartItem> findByOwnerEmail(@Param("email") String email);

    /**
     * 특정 장바구니에서 특정 상품의 아이템을 조회합니다.
     * 
     * @param cart 장바구니
     * @param product 상품
     * @return 해당 장바구니의 해당 상품 아이템
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    /**
     * 장바구니 ID와 상품 ID로 아이템을 조회합니다.
     * 
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @return 해당 조건의 장바구니 아이템
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cno = :cartId AND ci.product.pno = :productId")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);

    /**
     * 회원 이메일과 상품 ID로 장바구니 아이템을 조회합니다.
     * 
     * @param email 회원 이메일
     * @param productId 상품 ID
     * @return 해당 조건의 장바구니 아이템
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.owner.email = :email AND ci.product.pno = :productId")
    Optional<CartItem> findByOwnerEmailAndProductId(@Param("email") String email, @Param("productId") Long productId);

    /**
     * 특정 상품이 포함된 모든 장바구니 아이템을 조회합니다.
     * 
     * @param product 상품
     * @return 해당 상품을 포함한 장바구니 아이템 목록
     */
    List<CartItem> findByProduct(Product product);

    /**
     * 특정 장바구니의 아이템 개수를 조회합니다.
     * 
     * @param cart 장바구니
     * @return 아이템 개수
     */
    long countByCart(Cart cart);

    /**
     * 장바구니 ID의 아이템 개수를 조회합니다.
     * 
     * @param cartId 장바구니 ID
     * @return 아이템 개수
     */
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.cno = :cartId")
    long countByCartId(@Param("cartId") Long cartId);

    /**
     * 회원 이메일의 장바구니 아이템 개수를 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 아이템 개수
     */
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.owner.email = :email")
    long countByOwnerEmail(@Param("email") String email);

    /**
     * 특정 장바구니의 총 상품 수량을 조회합니다.
     * 
     * @param cart 장바구니
     * @return 총 상품 수량
     */
    @Query("SELECT SUM(ci.qty) FROM CartItem ci WHERE ci.cart = :cart")
    Integer getTotalQuantityByCart(@Param("cart") Cart cart);

    /**
     * 회원 이메일의 장바구니 총 상품 수량을 조회합니다.
     * 
     * @param email 회원 이메일
     * @return 총 상품 수량
     */
    @Query("SELECT SUM(ci.qty) FROM CartItem ci WHERE ci.cart.owner.email = :email")
    Integer getTotalQuantityByOwnerEmail(@Param("email") String email);

    /**
     * 특정 장바구니의 모든 아이템을 삭제합니다.
     * 
     * @param cart 장바구니
     * @return 삭제된 아이템 수
     */
    long deleteByCart(Cart cart);

    /**
     * 장바구니 ID의 모든 아이템을 삭제합니다.
     * 
     * @param cartId 장바구니 ID
     * @return 삭제된 아이템 수
     */
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cno = :cartId")
    int deleteByCartId(@Param("cartId") Long cartId);

    /**
     * 회원 이메일의 장바구니 아이템을 모두 삭제합니다.
     * 
     * @param email 회원 이메일
     * @return 삭제된 아이템 수
     */
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.owner.email = :email")
    int deleteByOwnerEmail(@Param("email") String email);

    /**
     * 특정 상품을 포함한 모든 장바구니 아이템을 삭제합니다.
     * 
     * @param product 상품
     * @return 삭제된 아이템 수
     */
    long deleteByProduct(Product product);

    /**
     * 특정 장바구니 아이템의 수량을 업데이트합니다.
     * 
     * @param cartItemId 장바구니 아이템 ID
     * @param qty 새로운 수량
     * @return 업데이트된 레코드 수
     */
    @Modifying
    @Query("UPDATE CartItem ci SET ci.qty = :qty WHERE ci.cino = :cartItemId")
    int updateQuantity(@Param("cartItemId") Long cartItemId, @Param("qty") int qty);

    /**
     * 장바구니와 상품으로 아이템의 수량을 업데이트합니다.
     * 
     * @param cartId 장바구니 ID
     * @param productId 상품 ID
     * @param qty 새로운 수량
     * @return 업데이트된 레코드 수
     */
    @Modifying
    @Query("UPDATE CartItem ci SET ci.qty = :qty WHERE ci.cart.cno = :cartId AND ci.product.pno = :productId")
    int updateQuantityByCartAndProduct(@Param("cartId") Long cartId, @Param("productId") Long productId, @Param("qty") int qty);
}
