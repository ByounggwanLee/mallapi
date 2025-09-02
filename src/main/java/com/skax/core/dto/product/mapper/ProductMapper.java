package com.skax.core.dto.product.mapper;

import com.skax.core.dto.product.request.ProductCreateRequest;
import com.skax.core.dto.product.request.ProductUpdateRequest;
import com.skax.core.dto.product.response.ProductResponse;
import com.skax.core.entity.product.Product;
import com.skax.core.entity.product.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    /**
     * ProductCreateRequest를 Product 엔티티로 변환합니다.
     * 
     * @param request Product 생성 요청 DTO
     * @return Product 엔티티
     */
    @Mapping(target = "pno", ignore = true)
    @Mapping(target = "pname", source = "productName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "pdesc", source = "description")
    @Mapping(target = "delFlag", constant = "false")
    @Mapping(target = "imageList", ignore = true)
    Product toEntity(ProductCreateRequest request);

    /**
     * Product 엔티티를 ProductResponse DTO로 변환합니다.
     * 
     * @param product Product 엔티티
     * @return ProductResponse DTO
     */
    @Mapping(target = "pno", source = "pno")
    @Mapping(target = "productName", source = "pname")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "pdesc")
    @Mapping(target = "deleted", source = "delFlag")
    @Mapping(target = "images", source = "imageList", qualifiedByName = "mapImages")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductResponse toResponse(Product product);

    /**
     * ProductUpdateRequest DTO를 사용하여 Product 엔티티를 업데이트합니다.
     * 
     * @param request ProductUpdateRequest DTO
     * @param product 수정할 Product 엔티티
     */
    default void updateFromRequest(ProductUpdateRequest request, @MappingTarget Product product) {
        if (request.getProductName() != null) {
            product.changeName(request.getProductName());
        }
        if (request.getDescription() != null) {
            product.changeDesc(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.changePrice(request.getPrice());
        }
    }

    /**
     * ProductImage List를 String List로 변환합니다.
     * 
     * @param imageList ProductImage List
     * @return String List (파일명 목록)
     */
    @Named("mapImages")
    default List<String> mapImages(List<ProductImage> imageList) {
        if (imageList == null) {
            return List.of();
        }
        return imageList.stream()
                .map(ProductImage::getFileName)
                .collect(Collectors.toList());
    }
}
