package com.example.mlservice.controller;

import com.example.mlservice.service.IRecommendationsService;
import com.google.cloud.recommendationengine.v1beta1.PredictResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {


    private final IRecommendationsService recommendationsService;

    public RecommendationsController(IRecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/location")
    public ResponseEntity<?> getRecommendedLocation(@RequestParam String productId, @RequestParam int quantity) {
        try {
            List<PredictResponse.PredictionResult> recommendedLocations = recommendationsService.getRecommendedLocations(productId, quantity);
            return ResponseEntity.ok(recommendedLocations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching recommendations");
        }
    }
}
