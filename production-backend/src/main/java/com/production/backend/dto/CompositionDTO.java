package com.production.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CompositionDTO {

    @NotNull
    private Integer resourceId;

    @NotNull
    @Positive
    private Integer qtdResource;
}
