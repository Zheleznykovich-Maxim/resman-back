package com.example.resmanback.controller;

import com.example.resmanback.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = analyticsService.generateAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/available-portions")
    public Map<String, Integer> getAvailablePortions() {
        return analyticsService.calculateAvailablePortions();
    }
}
