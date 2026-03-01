package com.production.backend.mapper;

import com.production.backend.dto.ResourceDTO;
import com.production.backend.entity.Resource;
import com.production.backend.response.ResourceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    Resource toEntity(ResourceDTO dto);

    @Mapping(source = "qtdStock", target = "stock")
    ResourceResponse toResponse(Resource entity);
}
