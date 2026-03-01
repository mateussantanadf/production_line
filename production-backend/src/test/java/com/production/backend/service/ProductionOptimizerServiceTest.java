package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.response.ProductionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductionOptimizerServiceTest {

    private StockService stockService;
    private ProductionOptimizerService optimizerService;

    @BeforeEach
    void setup() {
        stockService = mock(StockService.class);
        optimizerService = new ProductionOptimizerService(stockService);
    }

    @Test
    void shouldSelectProductWithHighestRevenueInGroup() {

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setName("Cake");
        p1.setPrice(BigDecimal.valueOf(10));

        Product p2 = new Product();
        p2.setCode(2L);
        p2.setName("Cookie");
        p2.setPrice(BigDecimal.valueOf(8));

        when(stockService.calculateMaxProduction(eq(p1), anyMap())).thenReturn(3);
        when(stockService.calculateMaxProduction(eq(p2), anyMap())).thenReturn(5);

        Map<Long, Integer> stock = new HashMap<>();

        List<List<Product>> groups = List.of(List.of(p1, p2));

        List<ProductionResponse> result =
                optimizerService.optimize(groups, stock);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getProductCode());
        assertEquals(BigDecimal.valueOf(40), result.get(0).getTotalValue());

        verify(stockService).updateStock(eq(p2), anyMap(), eq(5));
    }

    @Test
    void shouldNotAddGroupIfNoProductCanBeProduced() {

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setPrice(BigDecimal.valueOf(10));

        when(stockService.calculateMaxProduction(eq(p1), anyMap()))
                .thenReturn(0);

        Map<Long, Integer> stock = new HashMap<>();
        List<List<Product>> groups = List.of(List.of(p1));

        List<ProductionResponse> result =
                optimizerService.optimize(groups, stock);

        assertTrue(result.isEmpty());

        verify(stockService, never())
                .updateStock(any(), anyMap(), anyInt());
    }

    @Test
    void shouldProcessMultipleGroupsIndependently() {

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setPrice(BigDecimal.valueOf(10));

        Product p2 = new Product();
        p2.setCode(2L);
        p2.setPrice(BigDecimal.valueOf(20));

        when(stockService.calculateMaxProduction(eq(p1), anyMap()))
                .thenReturn(2);

        when(stockService.calculateMaxProduction(eq(p2), anyMap()))
                .thenReturn(1);

        Map<Long, Integer> stock = new HashMap<>();

        List<List<Product>> groups =
                List.of(
                        List.of(p1),
                        List.of(p2)
                );

        List<ProductionResponse> result =
                optimizerService.optimize(groups, stock);

        assertEquals(2, result.size());

        verify(stockService, times(2))
                .updateStock(any(), anyMap(), anyInt());
    }
}