package com.production.backend.mapper;

import com.production.backend.entity.Composition;
import com.production.backend.response.CompositionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompositionMapper {

    @Mapping(source = "resource.code", target = "resourceCode")
    @Mapping(source = "resource.name", target = "resourceName")
    @Mapping(source = "qtd", target = "qtdResource")
    CompositionResponse toResponse(Composition composition);
}
