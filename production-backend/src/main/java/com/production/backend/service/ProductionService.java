package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.repository.ProductRepository;
import com.production.backend.response.ProductionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductRepository productRepository;
    private final ConflictService conflictService;
    private final StockService stockService;
    private final ProductionOptimizerService optimizerService;

    public List<ProductionResponse> suggestBestProductionGain() {

        List<Product> products = productRepository.findAll();

        Map<Long, Integer> stock = stockService.loadStock();

        List<List<Product>> groups =
                conflictService.buildConflictGroups(products);

        return optimizerService.optimize(groups, stock);
    }
}
