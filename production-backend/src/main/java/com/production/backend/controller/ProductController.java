package com.production.backend.controller;

import com.production.backend.dto.ProductDTO;
import com.production.backend.response.ProductResponse;
import com.production.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{code}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long code,
            @Valid @RequestBody ProductDTO dto) {
        ProductResponse response = service.update(code, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable Long code) {

        return ResponseEntity.ok(service.findById(code));
    }
}
