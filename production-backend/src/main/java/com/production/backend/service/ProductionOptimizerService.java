package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.response.ProductionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionOptimizerService {

    private final StockService stockService;

    public List<ProductionResponse> optimize(
            List<List<Product>> groups,
            Map<Long, Integer> stock) {

        List<ProductionResponse> result = new ArrayList<>();

        for (List<Product> group : groups) {
            selectBestProductFromGroup(group, stock)
                    .ifPresent(result::add);
        }

        return result;
    }

    private Optional<ProductionResponse> selectBestProductFromGroup(
            List<Product> group,
            Map<Long, Integer> stock) {

        Product best = null;
        BigDecimal bestRevenue = BigDecimal.ZERO;
        int bestQty = 0;

        for (Product product : group) {

            int max = stockService.calculateMaxProduction(product, stock);
            if (max <= 0) continue;

            BigDecimal revenue =
                    product.getPrice().multiply(BigDecimal.valueOf(max));

            if (revenue.compareTo(bestRevenue) > 0) {
                best = product;
                bestRevenue = revenue;
                bestQty = max;
            }
        }

        if (best == null) return Optional.empty();

        stockService.updateStock(best, stock, bestQty);

        return Optional.of(buildResponse(best, bestQty, bestRevenue));
    }

    private ProductionResponse buildResponse(
            Product product,
            int quantity,
            BigDecimal total) {

        ProductionResponse response = new ProductionResponse();
        response.setProductCode(product.getCode());
        response.setProductName(product.getName());
        response.setQuantityToProduce(quantity);
        response.setUnitPrice(product.getPrice());
        response.setTotalValue(total);
        return response;
    }
}