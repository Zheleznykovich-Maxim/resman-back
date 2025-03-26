package com.example.resmanback.controller;

import com.example.resmanback.service.DataGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate")
public class DataGenerationController {

    private final DataGenerationService dataGenerationService;

    public DataGenerationController(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> generateOrders(@RequestParam int count) {
        dataGenerationService.generateOrders(count);
        return ResponseEntity.ok("Orders generated successfully!");
    }

    @PostMapping("/dishes")
    public ResponseEntity<String> generateDishes(@RequestParam int count) {
        dataGenerationService.generateDishes(count);
        return ResponseEntity.ok("Dishes generated successfully!");
    }
}
