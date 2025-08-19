package com.skax.core.entity.product;

import com.skax.core.entity.BaseEntity;
import com.skax.core.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 상품 정보를 나타내는 엔티티 클래스
 * 
 * <p>쇼핑몰의 상품 정보를 관리합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_name", columnList = "product_name"),
    @Index(name = "idx_product_category", columnList = "category"),
    @Index(name = "idx_product_price", columnList = "price"),
    @Index(name = "idx_product_available", columnList = "is_available")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    /**
     * 상품 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    /**
     * 상품명
     */
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    /**
     * 상품 설명
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 상품 가격
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 상품 카테고리
     */
    @Column(name = "category", length = 100)
    private String category;

    /**
     * 재고 수량
     */
    @Column(name = "stock_quantity", nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    /**
     * 상품 이미지 URL
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * 판매 가능 여부
     */
    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    /**
     * 상품 상태 (NEW, SALE, SOLD_OUT 등)
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "NEW";

    /**
     * 상품 등록자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 재고를 감소시킵니다.
     * 
     * @param quantity 감소할 수량
     * @throws IllegalArgumentException 재고가 부족한 경우
     */
    public void decreaseStock(Integer quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
        
        if (this.stockQuantity == 0) {
            this.status = "SOLD_OUT";
            this.isAvailable = false;
        }
    }

    /**
     * 재고를 증가시킵니다.
     * 
     * @param quantity 증가할 수량
     */
    public void increaseStock(Integer quantity) {
        this.stockQuantity += quantity;
        
        if (this.stockQuantity > 0 && "SOLD_OUT".equals(this.status)) {
            this.status = "SALE";
            this.isAvailable = true;
        }
    }

    /**
     * 상품 정보를 업데이트합니다.
     * 
     * @param productName 새로운 상품명
     * @param description 새로운 설명
     * @param price 새로운 가격
     * @param category 새로운 카테고리
     * @param imageUrl 새로운 이미지 URL
     */
    public void updateProduct(String productName, String description, BigDecimal price, 
                             String category, String imageUrl) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    /**
     * 상품 판매 상태를 변경합니다.
     * 
     * @param isAvailable 판매 가능 여부
     */
    public void changeAvailability(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        
        if (!isAvailable) {
            this.status = "UNAVAILABLE";
        } else if (this.stockQuantity > 0) {
            this.status = "SALE";
        }
    }
}
