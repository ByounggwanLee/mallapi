package com.skax.core.dto.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품 응답 DTO
 * 
 * <p>상품 정보를 클라이언트에 전달할 때 사용하는 데이터 전송 객체입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 응답")
public class ProductResponse {

    /**
     * 상품 번호
     */
    @Schema(description = "상품 번호", example = "1")
    private Long pno;

    /**
     * 상품명
     */
    @Schema(description = "상품명", example = "iPhone 15 Pro")
    private String productName;

    /**
     * 상품 설명
     */
    @Schema(description = "상품 설명", example = "최신 iPhone 15 Pro 모델")
    private String description;

    /**
     * 상품 가격
     */
    @Schema(description = "상품 가격", example = "1290000")
    private Integer price;

    /**
     * 상품 이미지 목록
     */
    @Schema(description = "상품 이미지 파일명 목록")
    private List<String> images;

    /**
     * 삭제 여부
     */
    @Schema(description = "삭제 여부", example = "false")
    private Boolean deleted;

    /**
     * 생성 시간
     */
    @Schema(description = "생성 시간", example = "2025-08-23T10:30:00")
    private LocalDateTime createdAt;

    /**
     * 수정 시간
     */
    @Schema(description = "수정 시간", example = "2025-08-23T15:45:00")
    private LocalDateTime updatedAt;
}
