package com.production.backend.controller;

import com.production.backend.response.ProductionResponse;
import com.production.backend.service.ProductionExecutionService;
import com.production.backend.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService service;
    private final ProductionExecutionService productionExecutionService;

    @GetMapping("/suggest")
    public List<ProductionResponse> suggest() {
        return service.suggestBestProductionGain();
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ProductionResponse> produce(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        ProductionResponse response =
                productionExecutionService.produce(productId, quantity);

        return ResponseEntity.ok(response);
    }
}
