package com.skax.core.entity.cart;

import com.skax.core.entity.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 장바구니 아이템 정보를 나타내는 엔티티 클래스
 * 
 * <p>장바구니에 담긴 개별 상품 정보를 관리합니다. 다음과 같은 정보를 포함합니다:</p>
 * <ul>
 *   <li>장바구니 아이템 고유 번호 (cino)</li>
 *   <li>상품 정보 (product)</li>
 *   <li>장바구니 정보 (cart)</li>
 *   <li>수량 (qty)</li>
 * </ul>
 * 
 * <p>하나의 장바구니에는 여러 아이템이 포함될 수 있으며, 각 아이템은 특정 상품과 수량 정보를 가집니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = "cart")
@Table(name = "tbl_cart_item", indexes = {
    @Index(columnList = "cart_cno", name = "idx_cartitem_cart"),
    @Index(columnList = "product_pno, cart_cno", name = "idx_cartitem_pno_cart")
})
public class CartItem {

  /**
   * 장바구니 아이템 고유 식별자
   * 데이터베이스에서 자동으로 생성되는 기본키입니다.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cino;

  /**
   * 장바구니에 담긴 상품
   * 다대일 관계로 여러 장바구니 아이템이 하나의 상품을 참조할 수 있습니다.
   */
  @ManyToOne
  @JoinColumn(name = "product_pno")
  private Product product;

  /**
   * 이 아이템이 속한 장바구니
   * 다대일 관계로 하나의 장바구니에 여러 아이템이 포함될 수 있습니다.
   */
  @ManyToOne
  @JoinColumn(name = "cart_cno")
  private Cart cart;

  /**
   * 상품 수량
   * 장바구니에 담긴 해당 상품의 개수입니다.
   */
  private int qty;

  /**
   * 장바구니 아이템의 수량을 변경합니다.
   * 
   * @param qty 새로운 수량 (1 이상의 값)
   * @throws IllegalArgumentException qty가 1보다 작은 경우
   */
  public void changeQty(int qty) {
    this.qty = qty;
  }

}
