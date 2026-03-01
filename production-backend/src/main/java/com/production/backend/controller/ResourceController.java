package com.production.backend.controller;

import com.production.backend.dto.ResourceDTO;
import com.production.backend.response.ResourceResponse;
import com.production.backend.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @PostMapping
    public ResponseEntity<ResourceResponse> create(
            @Valid @RequestBody ResourceDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable Long code,
            @Valid @RequestBody ResourceDTO dto) {

        return ResponseEntity.ok(service.update(code, dto));
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ResourceResponse> findById(
            @PathVariable Long code) {

        return ResponseEntity.ok(service.findById(code));
    }
}
