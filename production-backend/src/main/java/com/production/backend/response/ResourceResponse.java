package com.production.backend.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceResponse {

    private Long code;
    private String name;
    private Integer stock;
}
