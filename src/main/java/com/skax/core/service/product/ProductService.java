package com.skax.core.service.product;

import com.skax.core.dto.product.request.ProductCreateRequest;
import com.skax.core.dto.product.request.ProductUpdateRequest;
import com.skax.core.dto.product.response.ProductResponse;
import com.skax.core.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * 상품 관리 서비스 인터페이스
 * 
 * <p>상품의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 정의합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>상품 등록, 수정, 삭제 (논리적 삭제)</li>
 *   <li>상품 목록 조회 (페이징)</li>
 *   <li>상품명, 설명 기반 검색</li>
 *   <li>가격 범위별 조회</li>
 *   <li>상품 이미지 관리</li>
 *   <li>상품 통계 정보</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
public interface ProductService {

    /**
     * 새로운 상품을 등록합니다.
     * 
     * @param request 상품 생성 요청 정보
     * @return 생성된 상품 정보
     * @throws IllegalArgumentException 필수 정보가 누락된 경우
     */
    ProductResponse createProduct(ProductCreateRequest request);

    /**
     * 상품 정보를 수정합니다.
     * 
     * @param pno 상품 번호
     * @param request 상품 수정 요청 정보
     * @return 수정된 상품 정보
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우
     */
    ProductResponse updateProduct(Long pno, ProductUpdateRequest request);

    /**
     * 상품을 삭제합니다 (논리적 삭제).
     * 
     * @param pno 삭제할 상품 번호
     * @throws IllegalArgumentException 존재하지 않는 상품인 경우
     */
    void deleteProduct(Long pno);

    /**
     * 삭제된 상품을 복구합니다.
     * 
     * @param pno 복구할 상품 번호
     * @throws IllegalArgumentException 존재하지 않는 상품인 경우
     */
    void restoreProduct(Long pno);

    /**
     * 상품 번호로 상품을 조회합니다.
     * 
     * @param pno 조회할 상품 번호
     * @return 상품 정보
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우
     */
    ProductResponse getProductById(Long pno);

    /**
     * 모든 활성 상품을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 상품 목록
     */
    PageResponse<ProductResponse> getAllProducts(Pageable pageable);

    /**
     * 상품명으로 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 상품 목록
     */
    PageResponse<ProductResponse> searchProductsByName(String keyword, Pageable pageable);

    /**
     * 상품명 또는 설명으로 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 상품 목록
     */
    PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable);

    /**
     * 가격 범위로 상품을 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이징 정보
     * @return 해당 가격 범위의 상품 목록
     */
    PageResponse<ProductResponse> getProductsByPriceRange(int minPrice, int maxPrice, Pageable pageable);

    /**
     * 복합 조건으로 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드 (선택)
     * @param minPrice 최소 가격 (선택)
     * @param maxPrice 최대 가격 (선택)
     * @param pageable 페이징 정보
     * @return 검색 조건에 맞는 상품 목록
     */
    PageResponse<ProductResponse> searchProductsByConditions(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable);

    /**
     * 상품에 이미지를 추가합니다.
     * 
     * @param pno 상품 번호
     * @param fileName 이미지 파일명
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우
     */
    void addProductImage(Long pno, String fileName);

    /**
     * 상품의 모든 이미지를 제거합니다.
     * 
     * @param pno 상품 번호
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우
     */
    void clearProductImages(Long pno);

    /**
     * 상품의 가격을 변경합니다.
     * 
     * @param pno 상품 번호
     * @param newPrice 새로운 가격
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우, 또는 가격이 음수인 경우
     */
    void changeProductPrice(Long pno, int newPrice);

    /**
     * 전체 활성 상품 수를 조회합니다.
     * 
     * @return 전체 활성 상품 수
     */
    long getTotalActiveProductCount();

    /**
     * 특정 가격 범위의 활성 상품 수를 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 해당 가격 범위의 상품 수
     */
    long getProductCountByPriceRange(int minPrice, int maxPrice);

    /**
     * 평균 상품 가격을 조회합니다.
     * 
     * @return 평균 상품 가격
     */
    double getAverageProductPrice();

    /**
     * 최고가 상품을 조회합니다.
     * 
     * @return 최고가 상품 정보
     */
    ProductResponse getMostExpensiveProduct();

    /**
     * 최저가 상품을 조회합니다.
     * 
     * @return 최저가 상품 정보
     */
    ProductResponse getCheapestProduct();
}
