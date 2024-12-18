package com.example.mlservice.controller;


import com.example.mlservice.service.IVertexAiPredictionService;
import com.google.protobuf.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/predict")
public class PredictionLocationController {
    @Autowired
    private     IVertexAiPredictionService predictionService;

    @PostMapping
    public List<Value> predict(@RequestBody List<String> featureData) {
        try {
            return predictionService.predict(featureData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error making prediction request", e);
        }
    }
}
