package com.skax.core.service.product.impl;

import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.product.request.ProductCreateRequest;
import com.skax.core.dto.product.request.ProductUpdateRequest;
import com.skax.core.dto.product.response.ProductResponse;
import com.skax.core.entity.product.Product;
import com.skax.core.repository.product.ProductRepository;
import com.skax.core.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 관리 서비스 구현체
 * 
 * <p>상품의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 구현합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        log.info("상품 생성 요청: {}", request);
        
        Product product = Product.builder()
                .pname(request.getProductName())
                .price(request.getPrice())
                .pdesc(request.getDescription())
                .category("기본") // 현재 DTO에 category 필드가 없으므로 기본값 설정
                .delFlag(false)
                .build();
        
        // 이미지 추가
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            request.getImages().forEach(product::addImageString);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("상품 생성 완료: pno={}", savedProduct.getPno());
        
        return convertToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long pno, ProductUpdateRequest request) {
        log.info("상품 수정 요청: pno={}, request={}", pno, request);
        
        Product product = getActiveProductEntity(pno);
        
        // 필드 업데이트
        if (request.getProductName() != null) {
            product.changeName(request.getProductName());
        }
        if (request.getPrice() != null) {
            product.changePrice(request.getPrice());
        }
        if (request.getDescription() != null) {
            product.changeDesc(request.getDescription());
        }
        
        // 이미지 업데이트
        if (request.getImages() != null) {
            product.clearList();
            request.getImages().forEach(product::addImageString);
        }
        
        log.info("상품 수정 완료: pno={}", pno);
        return convertToResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long pno) {
        log.info("상품 삭제 요청: pno={}", pno);
        
        Product product = getProductEntity(pno);
        product.changeDel(true);
        
        log.info("상품 삭제 완료: pno={}", pno);
    }

    @Override
    @Transactional
    public void restoreProduct(Long pno) {
        log.info("상품 복구 요청: pno={}", pno);
        
        Product product = getProductEntity(pno);
        product.changeDel(false);
        
        log.info("상품 복구 완료: pno={}", pno);
    }

    @Override
    public ProductResponse getProductById(Long pno) {
        log.debug("상품 조회 요청: pno={}", pno);
        
        Product product = getActiveProductEntity(pno);
        return convertToResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        log.debug("전체 상품 조회 요청: pageable={}", pageable);
        
        Page<Product> productPage = productRepository.findByDelFlagFalse(pageable);
        return convertToPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> searchProductsByName(String keyword, Pageable pageable) {
        log.debug("상품명 검색 요청: keyword={}, pageable={}", keyword, pageable);
        
        Page<Product> productPage = productRepository.findByPnameContainingAndDelFlagFalse(keyword, pageable);
        return convertToPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        log.debug("상품 검색 요청: keyword={}, pageable={}", keyword, pageable);
        
        // 간단한 구현으로 상품명만 검색 (실제로는 OR 조건으로 설명도 포함해야 함)
        Page<Product> productPage = productRepository.findByPnameContainingAndDelFlagFalse(keyword, pageable);
        return convertToPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByPriceRange(int minPrice, int maxPrice, Pageable pageable) {
        log.debug("가격 범위 상품 조회 요청: minPrice={}, maxPrice={}, pageable={}", minPrice, maxPrice, pageable);
        
        Page<Product> productPage = productRepository.findByPriceBetweenAndDelFlagFalse(minPrice, maxPrice, pageable);
        return convertToPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> searchProductsByConditions(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {
        log.debug("복합 조건 상품 검색 요청: keyword={}, minPrice={}, maxPrice={}, pageable={}", keyword, minPrice, maxPrice, pageable);
        
        // 간단한 구현 - 실제로는 Specification이나 QueryDSL을 사용하는 것이 좋음
        Page<Product> productPage;
        
        if (keyword != null && minPrice != null && maxPrice != null) {
            // 복합 조건은 일단 키워드 검색만 처리
            productPage = productRepository.findByPnameContainingAndDelFlagFalse(keyword, pageable);
        } else if (keyword != null) {
            productPage = productRepository.findByPnameContainingAndDelFlagFalse(keyword, pageable);
        } else if (minPrice != null && maxPrice != null) {
            productPage = productRepository.findByPriceBetweenAndDelFlagFalse(minPrice, maxPrice, pageable);
        } else {
            productPage = productRepository.findByDelFlagFalse(pageable);
        }
        
        return convertToPageResponse(productPage);
    }

    @Override
    @Transactional
    public void addProductImage(Long pno, String fileName) {
        log.info("상품 이미지 추가 요청: pno={}, fileName={}", pno, fileName);
        
        Product product = getActiveProductEntity(pno);
        product.addImageString(fileName);
        
        log.info("상품 이미지 추가 완료: pno={}", pno);
    }

    @Override
    @Transactional
    public void clearProductImages(Long pno) {
        log.info("상품 이미지 전체 삭제 요청: pno={}", pno);
        
        Product product = getActiveProductEntity(pno);
        product.clearList();
        
        log.info("상품 이미지 전체 삭제 완료: pno={}", pno);
    }

    @Override
    @Transactional
    public void changeProductPrice(Long pno, int newPrice) {
        log.info("상품 가격 변경 요청: pno={}, newPrice={}", pno, newPrice);
        
        if (newPrice < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다: " + newPrice);
        }
        
        Product product = getActiveProductEntity(pno);
        product.changePrice(newPrice);
        
        log.info("상품 가격 변경 완료: pno={}", pno);
    }

    @Override
    public long getTotalActiveProductCount() {
        return productRepository.countByDelFlagFalse();
    }

    @Override
    public long getProductCountByPriceRange(int minPrice, int maxPrice) {
        return productRepository.countByPriceBetweenAndDelFlagFalse(minPrice, maxPrice);
    }

    @Override
    public double getAverageProductPrice() {
        // Repository에 해당 메서드가 없으므로 임시로 0.0 반환
        // 실제로는 @Query 어노테이션으로 구현해야 함
        return 0.0;
    }

    @Override
    public ProductResponse getMostExpensiveProduct() {
        Product product = productRepository.findTopByDelFlagFalseOrderByPriceDesc()
                .orElseThrow(() -> new IllegalArgumentException("등록된 상품이 없습니다"));
        return convertToResponse(product);
    }

    @Override
    public ProductResponse getCheapestProduct() {
        Product product = productRepository.findTopByDelFlagFalseOrderByPriceAsc()
                .orElseThrow(() -> new IllegalArgumentException("등록된 상품이 없습니다"));
        return convertToResponse(product);
    }

    /**
     * 상품 엔티티를 조회합니다 (삭제된 상품 포함).
     * 
     * @param pno 상품 번호
     * @return 상품 엔티티
     * @throws IllegalArgumentException 존재하지 않는 상품인 경우
     */
    private Product getProductEntity(Long pno) {
        return productRepository.findById(pno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + pno));
    }

    /**
     * 활성 상품 엔티티를 조회합니다 (삭제된 상품 제외).
     * 
     * @param pno 상품 번호
     * @return 활성 상품 엔티티
     * @throws IllegalArgumentException 존재하지 않는 상품이거나 삭제된 상품인 경우
     */
    private Product getActiveProductEntity(Long pno) {
        Product product = getProductEntity(pno);
        if (product.isDelFlag()) {
            throw new IllegalArgumentException("삭제된 상품입니다: " + pno);
        }
        return product;
    }

    /**
     * Product 엔티티를 ProductResponse로 변환합니다.
     * 
     * @param product 상품 엔티티
     * @return ProductResponse
     */
    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .pno(product.getPno())
                .productName(product.getPname())
                .price(product.getPrice())
                .description(product.getPdesc())
                .deleted(product.isDelFlag())
                .images(product.getImageList().stream()
                        .map(image -> image.getFileName())
                        .toList())
                .build();
    }

    /**
     * Page<Product>를 PageResponse<ProductResponse>로 변환합니다.
     * 
     * @param productPage 상품 페이지
     * @return PageResponse<ProductResponse>
     */
    private PageResponse<ProductResponse> convertToPageResponse(Page<Product> productPage) {
        return PageResponse.<ProductResponse>builder()
                .content(productPage.getContent().stream()
                        .map(this::convertToResponse)
                        .toList())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .build();
    }
}
