package com.production.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponse {

    private Long code;
    private String name;
    private BigDecimal price;
    private List<CompositionResponse> compositions;
}
