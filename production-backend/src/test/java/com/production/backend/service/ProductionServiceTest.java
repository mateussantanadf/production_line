package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.repository.ProductRepository;
import com.production.backend.response.ProductionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductionServiceTest {

    private ProductRepository productRepository;
    private ConflictService conflictService;
    private StockService stockService;
    private ProductionOptimizerService optimizerService;

    private ProductionService productionService;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        conflictService = mock(ConflictService.class);
        stockService = mock(StockService.class);
        optimizerService = mock(ProductionOptimizerService.class);

        productionService = new ProductionService(
                productRepository,
                conflictService,
                stockService,
                optimizerService
        );
    }

    @Test
    void shouldCallAllDependenciesAndReturnOptimizerResult() {

        List<Product> products = List.of(new Product());
        Map<Long, Integer> stock = new HashMap<>();
        List<List<Product>> groups = List.of(List.of(new Product()));
        List<ProductionResponse> expected = List.of(new ProductionResponse());

        when(productRepository.findAll()).thenReturn(products);
        when(stockService.loadStock()).thenReturn(stock);
        when(conflictService.buildConflictGroups(products)).thenReturn(groups);
        when(optimizerService.optimize(groups, stock)).thenReturn(expected);

        List<ProductionResponse> result =
                productionService.suggestBestProductionGain();

        assertEquals(expected, result);

        verify(productRepository).findAll();
        verify(stockService).loadStock();
        verify(conflictService).buildConflictGroups(products);
        verify(optimizerService).optimize(groups, stock);
    }
}