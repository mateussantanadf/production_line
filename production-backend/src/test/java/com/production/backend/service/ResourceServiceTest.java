package com.production.backend.service;

import com.production.backend.dto.ResourceDTO;
import com.production.backend.entity.Resource;
import com.production.backend.mapper.ResourceMapper;
import com.production.backend.repository.ResourceRepository;
import com.production.backend.response.ResourceResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    private ResourceRepository repository;
    private ResourceMapper mapper;
    private ResourceService service;

    @BeforeEach
    void setup() {
        repository = mock(ResourceRepository.class);
        mapper = mock(ResourceMapper.class);
        service = new ResourceService(repository, mapper);
    }

    @Test
    void shouldCreateResource() {

        ResourceDTO dto = new ResourceDTO();
        Resource entity = new Resource();
        Resource saved = new Resource();
        ResourceResponse response = new ResourceResponse();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        ResourceResponse result = service.create(dto);

        assertEquals(response, result);
        verify(repository).save(entity);
    }

    @Test
    void shouldUpdateResource() {

        Long code = 1L;

        Resource existing = new Resource();
        existing.setName("Old");
        existing.setQtdStock(10);

        ResourceDTO dto = new ResourceDTO();
        dto.setName("New");
        dto.setQtdStock(20);

        ResourceResponse response = new ResourceResponse();

        when(repository.findById(code))
                .thenReturn(Optional.of(existing));

        when(mapper.toResponse(existing))
                .thenReturn(response);

        ResourceResponse result = service.update(code, dto);

        assertEquals("New", existing.getName());
        assertEquals(20, existing.getQtdStock());
        assertEquals(response, result);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingResource() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.update(1L, new ResourceDTO()));
    }

    @Test
    void shouldReturnAllResources() {

        Resource entity = new Resource();
        ResourceResponse response = new ResourceResponse();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<ResourceResponse> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void shouldReturnResourceById() {

        Resource entity = new Resource();
        ResourceResponse response = new ResourceResponse();

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(mapper.toResponse(entity)).thenReturn(response);

        ResourceResponse result = service.findById(1L);

        assertEquals(response, result);
    }

    @Test
    void shouldThrowWhenFindingByIdNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldReturnEntityByCode() {

        Resource entity = new Resource();

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        Resource result = service.findEntityByCode(1L);

        assertEquals(entity, result);
    }

    @Test
    void shouldThrowEntityNotFoundWhenFindEntityByCodeFails() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.findEntityByCode(1L));
    }
}