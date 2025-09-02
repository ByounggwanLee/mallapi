package com.skax.core.controller.product;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.product.request.ProductCreateRequest;
import com.skax.core.dto.product.request.ProductUpdateRequest;
import com.skax.core.dto.product.response.ProductResponse;
import com.skax.core.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 관리 컨트롤러
 * 
 * <p>상품의 생성, 조회, 수정, 삭제 등의 REST API를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "상품 관리 API")
public class ProductController {

    private final ProductService productService;

    /**
     * 새로운 상품을 등록합니다.
     * 
     * @param request 상품 생성 요청 데이터
     * @return 생성된 상품 정보
     */
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public AxResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        log.info("상품 등록 요청 - 상품명: {}", request.getProductName());
        
        ProductResponse product = productService.createProduct(request);
        return AxResponseEntity.created(product, "상품이 성공적으로 등록되었습니다.");
    }

    /**
     * 상품 정보를 조회합니다.
     * 
     * @param pno 상품 번호
     * @return 상품 정보
     */
    @Operation(summary = "상품 정보 조회", description = "상품 번호로 상품 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{pno}")
    public AxResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "상품 번호", example = "1")
            @PathVariable Long pno) {
        log.info("상품 정보 조회 - 상품 번호: {}", pno);
        
        ProductResponse product = productService.getProductById(pno);
        return AxResponseEntity.ok(product, "상품 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 상품 정보를 수정합니다.
     * 
     * @param pno 상품 번호
     * @param request 상품 수정 요청 데이터
     * @return 수정된 상품 정보
     */
    @Operation(summary = "상품 정보 수정", description = "상품 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PutMapping("/{pno}")
    public AxResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "상품 번호", example = "1")
            @PathVariable Long pno,
            @Valid @RequestBody ProductUpdateRequest request) {
        log.info("상품 정보 수정 - 상품 번호: {}", pno);
        
        ProductResponse product = productService.updateProduct(pno, request);
        return AxResponseEntity.updated(product, "상품 정보가 성공적으로 수정되었습니다.");
    }

    /**
     * 상품을 삭제합니다.
     * 
     * @param pno 상품 번호
     */
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다 (논리적 삭제).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @DeleteMapping("/{pno}")
    public AxResponseEntity<Void> deleteProduct(
            @Parameter(description = "상품 번호", example = "1")
            @PathVariable Long pno) {
        log.info("상품 삭제 - 상품 번호: {}", pno);
        
        productService.deleteProduct(pno);
        return AxResponseEntity.deleted("상품이 성공적으로 삭제되었습니다.");
    }

    /**
     * 모든 상품을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 상품 목록
     */
    @Operation(summary = "상품 목록 조회", description = "모든 상품을 페이징하여 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping
    public AxResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("상품 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        PageResponse<ProductResponse> products = productService.getAllProducts(pageable);
        return AxResponseEntity.okPage(products, "상품 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 상품명으로 상품을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 상품 목록
     */
    @Operation(summary = "상품 검색", description = "상품명으로 상품을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상품 검색 성공")
    })
    @GetMapping("/search")
    public AxResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @Parameter(description = "검색 키워드", example = "iPhone")
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("상품 검색 - 키워드: {}", keyword);
        
        PageResponse<ProductResponse> products = productService.searchProductsByName(keyword, pageable);
        return AxResponseEntity.okPage(products, "상품 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 가격 범위로 상품을 조회합니다.
     * 
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param pageable 페이징 정보
     * @return 해당 가격 범위의 상품 목록
     */
    @Operation(summary = "가격 범위 상품 조회", description = "가격 범위로 상품을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "가격 범위 상품 조회 성공")
    })
    @GetMapping("/price-range")
    public AxResponseEntity<PageResponse<ProductResponse>> getProductsByPriceRange(
            @Parameter(description = "최소 가격", example = "100000")
            @RequestParam int minPrice,
            @Parameter(description = "최대 가격", example = "2000000")
            @RequestParam int maxPrice,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("가격 범위 상품 조회 - 최소: {}, 최대: {}", minPrice, maxPrice);
        
        PageResponse<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        return AxResponseEntity.okPage(products, "가격 범위 상품을 성공적으로 조회했습니다.");
    }
}
