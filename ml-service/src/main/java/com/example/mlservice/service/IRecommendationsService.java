package com.example.mlservice.service;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.recommendationengine.v1beta1.PredictResponse;

import java.util.List;

public interface IRecommendationsService {
    public List<PredictResponse.PredictionResult> getRecommendedLocations(String productId, int quantity);
}
