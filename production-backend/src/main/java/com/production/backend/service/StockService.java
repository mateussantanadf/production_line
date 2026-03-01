package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.entity.Composition;
import com.production.backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ResourceRepository resourceRepository;

    public Map<Long, Integer> loadStock() {
        return resourceRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Resource::getCode,
                        Resource::getQtdStock
                ));
    }

    public int calculateMaxProduction(Product product, Map<Long, Integer> stock) {

        return product.getCompositions()
                .stream()
                .mapToInt(comp ->
                        stock.get(comp.getResource().getCode()) / comp.getQtd()
                )
                .min()
                .orElse(0);
    }

    public void updateStock(Product product,
                            Map<Long, Integer> stock,
                            int quantityProduced) {

        for (Composition comp : product.getCompositions()) {

            Long resourceCode = comp.getResource().getCode();
            int required = comp.getQtd() * quantityProduced;

            stock.compute(resourceCode, (key, value) -> {

                if (value == null) {
                    throw new IllegalStateException("Resource not found in stock");
                }

                if (value < required) {
                    throw new IllegalStateException("Insufficient stock");
                }

                return value - required;
            });
        }
    }
}