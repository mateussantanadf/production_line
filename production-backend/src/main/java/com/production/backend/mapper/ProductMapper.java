package com.production.backend.mapper;

import com.production.backend.dto.ProductDTO;
import com.production.backend.entity.Product;
import com.production.backend.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    Product toEntity(ProductDTO dto);
}
