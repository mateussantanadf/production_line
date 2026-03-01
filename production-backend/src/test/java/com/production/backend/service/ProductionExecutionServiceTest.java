package com.production.backend.service;

import com.production.backend.entity.Composition;
import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.repository.ProductRepository;
import com.production.backend.response.ProductionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductionExecutionServiceTest {

    private ProductRepository productRepository;
    private ProductionExecutionService service;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        service = new ProductionExecutionService(productRepository);
    }

    @Test
    void shouldProduceSuccessfullyAndReduceStock() {

        Resource flour = new Resource();
        flour.setName("Flour");
        flour.setQtdStock(10);

        Composition comp = new Composition();
        comp.setResource(flour);
        comp.setQtd(2);

        Product product = new Product();
        product.setCode(1L);
        product.setName("Cake");
        product.setPrice(BigDecimal.valueOf(5));
        product.setCompositions(List.of(comp));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductionResponse response = service.produce(1L, 3);

        assertEquals(1L, response.getProductCode());
        assertEquals("Cake", response.getProductName());
        assertEquals(3, response.getQuantityToProduce());
        assertEquals(BigDecimal.valueOf(15), response.getTotalValue());

        assertEquals(4, flour.getQtdStock());
    }

    @Test
    void shouldThrowWhenProductNotFound() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.produce(1L, 2));
    }

    @Test
    void shouldThrowWhenInsufficientStock() {

        Resource flour = new Resource();
        flour.setName("Flour");
        flour.setQtdStock(3);

        Composition comp = new Composition();
        comp.setResource(flour);
        comp.setQtd(2);

        Product product = new Product();
        product.setCode(1L);
        product.setName("Cake");
        product.setPrice(BigDecimal.valueOf(5));
        product.setCompositions(List.of(comp));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class,
                () -> service.produce(1L, 2));
    }
}