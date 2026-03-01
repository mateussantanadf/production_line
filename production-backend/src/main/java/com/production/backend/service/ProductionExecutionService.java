package com.production.backend.service;

import com.production.backend.entity.Composition;
import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.repository.ProductRepository;
import com.production.backend.response.ProductionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductionExecutionService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductionResponse produce(Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (Composition comp : product.getCompositions()) {

            Resource resource = comp.getResource();

            int required = comp.getQtd() * quantity;

            if (resource.getQtdStock() < required) {
                throw new IllegalStateException(
                        "Insufficient stock for resource: "
                                + resource.getName());
            }

            resource.setQtdStock(resource.getQtdStock() - required);
        }

        return buildResponse(product, quantity);
    }

    private ProductionResponse buildResponse(Product product, int quantity) {

        ProductionResponse response = new ProductionResponse();
        response.setProductCode(product.getCode());
        response.setProductName(product.getName());
        response.setQuantityToProduce(quantity);
        response.setUnitPrice(product.getPrice());
        response.setTotalValue(
                product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity)));

        return response;
    }
}
