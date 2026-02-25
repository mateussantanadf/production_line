package com.production.backend.controller;

import com.production.backend.dto.ProductDTO;
import com.production.backend.response.ProductResponse;
import com.production.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductDTO dto) {
        ProductResponse response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
