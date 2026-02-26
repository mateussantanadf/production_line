package com.production.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResourceDTO {

    @NotBlank
    private String name;

    @NotNull
    private Integer qtdStock;
}
