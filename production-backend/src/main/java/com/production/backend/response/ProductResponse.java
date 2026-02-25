package com.production.backend.response;

import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
public class ProductResponse {

    private Integer code;
    private String name;
    private BigDecimal value;
    private List<CompositionResponse> compositions;
}
