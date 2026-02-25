package com.production.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductDTO {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal value;

    @Valid
    private List<CompositionDTO> compositions;
}
