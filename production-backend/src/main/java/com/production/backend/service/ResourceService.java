package com.production.backend.service;

import com.production.backend.dto.ResourceDTO;
import com.production.backend.entity.Resource;
import com.production.backend.mapper.ResourceMapper;
import com.production.backend.repository.ResourceRepository;
import com.production.backend.response.ResourceResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Transactional
    public ResourceResponse create(ResourceDTO dto) {

        Resource resource = mapper.toEntity(dto);

        Resource saved = repository.save(resource);

        return mapper.toResponse(saved);
    }

    @Transactional
    public ResourceResponse update(Long code, ResourceDTO dto) {

        Resource resource = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setName(dto.getName());
        resource.setQtdStock(dto.getQtdStock());

        return mapper.toResponse(resource);
    }

    public List<ResourceResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ResourceResponse findById(Long code) {

        Resource resource = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        return mapper.toResponse(resource);
    }

    public Resource findEntityByCode(Long code) {

        return repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));
    }
}
