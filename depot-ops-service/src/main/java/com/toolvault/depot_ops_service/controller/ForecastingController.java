package com.toolvault.depot_ops_service.controller;

import com.toolvault.depot_ops_service.service.ForecastingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forecasting")
@RequiredArgsConstructor
public class ForecastingController {

    private final ForecastingService forecastingService;

    @GetMapping("/frequency")
    public ResponseEntity<Map<String, Long>> getCheckoutFrequency() {
        return ResponseEntity.ok(forecastingService.getCheckoutFrequency());
    }

    @GetMapping("/risk")
    public ResponseEntity<List<String>> getLowStockRisk() {
        return ResponseEntity.ok(forecastingService.getLowStockRisk());
    }

    @GetMapping("/reorder")
    public ResponseEntity<Map<String, Long>> getReorderSuggestions() {
        return ResponseEntity.ok(forecastingService.getReorderSuggestions());
    }

}

