package com.skax.core.repository.product;

import com.skax.core.entity.product.Product;
import com.skax.core.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 상품 정보 접근을 위한 Repository 인터페이스
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 판매 가능한 상품들을 페이징으로 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 판매 가능한 상품 목록
     */
    Page<Product> findByIsAvailableTrue(Pageable pageable);

    /**
     * 카테고리별 상품을 조회합니다.
     * 
     * @param category 카테고리
     * @param pageable 페이징 정보
     * @return 카테고리별 상품 목록
     */
    Page<Product> findByCategory(String category, Pageable pageable);

    /**
     * 카테고리별 판매 가능한 상품을 조회합니다.
     * 
     * @param category 카테고리
     * @param pageable 페이징 정보
     * @return 카테고리별 판매 가능한 상품 목록
     */
    Page<Product> findByCategoryAndIsAvailableTrue(String category, Pageable pageable);

    /**
     * 특정 회원이 등록한 상품들을 조회합니다.
     * 
     * @param member 회원 정보
     * @param pageable 페이징 정보
     * @return 회원이 등록한 상품 목록
     */
    Page<Product> findByMember(Member member, Pageable pageable);

    /**
     * 특정 회원의 상품을 ID로 조회합니다.
     * 
     * @param id 상품 ID
     * @param member 회원 정보
     * @return 상품 정보
     */
    Optional<Product> findByIdAndMember(Long id, Member member);

    /**
     * 상품명으로 검색합니다.
     * 
     * @param productName 상품명
     * @param pageable 페이징 정보
     * @return 검색된 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:productName% AND p.isAvailable = true")
    Page<Product> searchByProductName(@Param("productName") String productName, Pageable pageable);

    /**
     * 가격 범위로 상품을 검색합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이징 정보
     * @return 가격 범위의 상품 목록
     */
    Page<Product> findByPriceBetweenAndIsAvailableTrue(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * 재고가 있는 상품들을 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 재고가 있는 상품 목록
     */
    Page<Product> findByStockQuantityGreaterThanAndIsAvailableTrue(Integer stockQuantity, Pageable pageable);

    /**
     * 상품 상태별로 조회합니다.
     * 
     * @param status 상품 상태
     * @param pageable 페이징 정보
     * @return 상태별 상품 목록
     */
    Page<Product> findByStatus(String status, Pageable pageable);

    /**
     * 인기 상품들을 조회합니다. (재고량과 판매 가능 여부 기준)
     * 
     * @param pageable 페이징 정보
     * @return 인기 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.isAvailable = true ORDER BY p.stockQuantity DESC, p.createdAt DESC")
    List<Product> findPopularProducts(Pageable pageable);

    /**
     * 카테고리별 상품 개수를 조회합니다.
     * 
     * @return 카테고리별 상품 개수 맵
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.isAvailable = true GROUP BY p.category")
    List<Object[]> countProductsByCategory();
}
