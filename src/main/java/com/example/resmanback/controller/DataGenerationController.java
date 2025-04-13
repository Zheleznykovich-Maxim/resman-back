package com.example.resmanback.controller;

import com.example.resmanback.model.dto.GenerateRequest;
import com.example.resmanback.model.dto.GenerateResponse;
import com.example.resmanback.service.DataGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
public class DataGenerationController {

    private final DataGenerationService dataGenerationService;

    public DataGenerationController(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @PostMapping("/dishes")
    public ResponseEntity<?> generateDishes(@RequestBody GenerateRequest request) {
        int generatedCount = dataGenerationService.generateDishes(request.getCount());
        return ResponseEntity.ok().body(new GenerateResponse(generatedCount, "dishes"));
    }

    @PostMapping("/ingredients")
    public ResponseEntity<?> generateIngredients(@RequestBody GenerateRequest request) {
        int generatedCount = dataGenerationService.generateIngredients(request.getCount());
        return ResponseEntity.ok().body(new GenerateResponse(generatedCount, "ingredients"));
    }

    @PostMapping("/orders")
    public ResponseEntity<?> generateOrders(@RequestBody GenerateRequest request) {
        int generatedCount = dataGenerationService.generateOrders(request.getCount());
        return ResponseEntity.ok().body(new GenerateResponse(generatedCount, "orders"));
    }
}
