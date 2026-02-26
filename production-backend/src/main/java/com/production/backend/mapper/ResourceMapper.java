package com.production.backend.mapper;

import com.production.backend.dto.ResourceDTO;
import com.production.backend.entity.Resource;
import com.production.backend.response.ResourceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    Resource toEntity(ResourceDTO dto);

    ResourceResponse toResponse(Resource entity);
}
