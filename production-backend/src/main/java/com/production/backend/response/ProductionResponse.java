package com.production.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductionResponse {

    private Long productCode;
    private String productName;
    private Integer quantityToProduce;
    private BigDecimal unitPrice;
    private BigDecimal totalValue;
}
