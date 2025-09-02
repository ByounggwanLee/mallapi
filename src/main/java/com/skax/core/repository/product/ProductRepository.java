package com.skax.core.repository.product;

import com.skax.core.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Product 엔티티에 대한 데이터 액세스 계층 인터페이스
 * 
 * <p>상품 데이터의 생성, 조회, 수정, 삭제 등의 데이터베이스 작업을 처리합니다.</p>
 * 
 * <p>제공하는 주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업</li>
 *   <li>상품명 기반 검색</li>
 *   <li>가격 범위별 상품 조회</li>
 *   <li>삭제 상태별 상품 조회</li>
 *   <li>복합 조건 검색</li>
 *   <li>페이징 처리</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 삭제되지 않은 상품 목록을 조회합니다.
     * 
     * @return 활성 상품 목록
     */
    List<Product> findByDeletedFalse();

    /**
     * 삭제되지 않은 상품 목록을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 활성 상품 목록
     */
    Page<Product> findByDeletedFalse(Pageable pageable);

    /**
     * 상품명에 특정 키워드가 포함된 활성 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    List<Product> findByPnameContainingAndDeletedFalse(String keyword);

    /**
     * 상품명에 특정 키워드가 포함된 활성 상품을 페이징하여 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 페이징된 검색 결과
     */
    Page<Product> findByPnameContainingAndDeletedFalse(String keyword, Pageable pageable);

    /**
     * 특정 가격 이상의 활성 상품을 조회합니다.
     * 
     * @param price 최소 가격
     * @return 해당 가격 이상의 상품 목록
     */
    List<Product> findByPriceGreaterThanEqualAndDeletedFalse(int price);

    /**
     * 특정 가격 이하의 활성 상품을 조회합니다.
     * 
     * @param price 최대 가격
     * @return 해당 가격 이하의 상품 목록
     */
    List<Product> findByPriceLessThanEqualAndDeletedFalse(int price);

    /**
     * 특정 가격 범위의 활성 상품을 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 해당 가격 범위의 상품 목록
     */
    List<Product> findByPriceBetweenAndDeletedFalse(int minPrice, int maxPrice);

    /**
     * 특정 가격 범위의 활성 상품을 페이징하여 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이징 정보
     * @return 페이징된 상품 목록
     */
    Page<Product> findByPriceBetweenAndDeletedFalse(int minPrice, int maxPrice, Pageable pageable);

    /**
     * 상품 ID로 활성 상품을 조회합니다.
     * 
     * @param pno 상품 ID
     * @return 해당 ID의 활성 상품
     */
    Optional<Product> findByPnoAndDeletedFalse(Long pno);

    /**
     * 복합 조건으로 상품을 검색합니다.
     * 
     * @param keyword 상품명 검색 키워드 (null 가능)
     * @param minPrice 최소 가격 (null 가능)
     * @param maxPrice 최대 가격 (null 가능)
     * @param pageable 페이징 정보
     * @return 검색 조건에 맞는 페이징된 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND " +
           "(:keyword IS NULL OR p.pname LIKE %:keyword%) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findByConditions(@Param("keyword") String keyword,
                                 @Param("minPrice") Integer minPrice,
                                 @Param("maxPrice") Integer maxPrice,
                                 Pageable pageable);

    /**
     * 상품 설명에 특정 키워드가 포함된 활성 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @return 설명에 키워드가 포함된 상품 목록
     */
    List<Product> findByPdescContainingAndDeletedFalse(String keyword);

    /**
     * 상품명 또는 설명에 키워드가 포함된 활성 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 페이징된 검색 결과
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND " +
           "(p.pname LIKE %:keyword% OR p.pdesc LIKE %:keyword%)")
    Page<Product> findByKeywordInNameOrDescription(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 활성 상품의 총 개수를 조회합니다.
     * 
     * @return 활성 상품 개수
     */
    long countByDeletedFalse();

    /**
     * 특정 가격 범위의 활성 상품 개수를 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 해당 가격 범위의 상품 개수
     */
    long countByPriceBetweenAndDeletedFalse(int minPrice, int maxPrice);
    
    /**
     * 상품명 존재 여부를 확인합니다.
     * 
     * @param pname 상품명
     * @return 존재 여부
     */
    boolean existsByPname(String pname);
    
    /**
     * 상품명을 포함하는 활성 상품을 검색합니다 (대소문자 무시).
     * 
     * @param pname 검색할 상품명
     * @param pageable 페이징 정보
     * @return 검색된 상품 목록
     */
    Page<Product> findByPnameContainingIgnoreCaseAndDeletedFalse(String pname, Pageable pageable);
    
    /**
     * 카테고리별 활성 상품을 조회합니다.
     * 
     * @param category 카테고리명
     * @param pageable 페이징 정보
     * @return 카테고리별 상품 목록
     */
    Page<Product> findByCategoryAndDeletedFalse(String category, Pageable pageable);
    
    /**
     * 카테고리별 활성 상품 수를 조회합니다.
     * 
     * @param category 카테고리명
     * @return 카테고리별 상품 수
     */
    long countByCategoryAndDeletedFalse(String category);
    
    /**
     * 활성 상품 중 최고가를 조회합니다.
     * 
     * @return 최고가 상품
     */
    Optional<Product> findTopByDeletedFalseOrderByPriceDesc();
    
    /**
     * 활성 상품 중 최저가를 조회합니다.
     * 
     * @return 최저가 상품
     */
    Optional<Product> findTopByDeletedFalseOrderByPriceAsc();
    
    /**
     * 활성 상품의 평균 가격을 조회합니다.
     * 
     * @return 평균 가격
     */
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.deleted = false")
    Double findAveragePriceOfActiveProducts();
}