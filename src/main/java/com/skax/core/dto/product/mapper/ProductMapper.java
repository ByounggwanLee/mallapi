package com.skax.core.dto.product.mapper;

import com.skax.core.dto.product.request.ProductCreateRequest;
import com.skax.core.dto.product.response.ProductResponse;
import com.skax.core.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Product 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Product 엔티티를 ProductResponse DTO로 변환합니다.
     * 
     * @param product Product 엔티티
     * @return ProductResponse DTO
     */
    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", source = "member.name")
    ProductResponse toResponse(Product product);

    /**
     * ProductCreateRequest DTO를 Product 엔티티로 변환합니다.
     * 
     * @param request ProductCreateRequest DTO
     * @return Product 엔티티
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAvailable", constant = "true")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "member", ignore = true)
    Product toEntity(ProductCreateRequest request);
}
