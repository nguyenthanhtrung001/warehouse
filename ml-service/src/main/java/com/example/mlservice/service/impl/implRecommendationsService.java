package com.example.mlservice.service.impl;

import com.example.mlservice.service.IRecommendationsService;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.recommendationengine.v1beta1.*;
import com.google.protobuf.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class implRecommendationsService  implements IRecommendationsService {
    private static final Logger logger = LoggerFactory.getLogger(implRecommendationsService.class);
    private final PredictionServiceClient predictionServiceClient;

    public implRecommendationsService() throws IOException {
        PredictionServiceSettings settings = PredictionServiceSettings.newBuilder()
                .setEndpoint("global-recommendationengine.googleapis.com:443")
                .build();
        this.predictionServiceClient = PredictionServiceClient.create(settings);
        logger.info("PredictionServiceClient initialized successfully.");
    }


    @Override
    public List<PredictResponse.PredictionResult> getRecommendedLocations(String productId, int quantity) {
        logger.info("Starting recommendation process for productId: {}, quantity: {}", productId, quantity);

        String projectId = "warehouse-439807";
        String location = "global";
        String catalogId = "default_catalog";
        String eventStoreId = "default_event_store";
        String placementId = "product_recommendation"; // Replace with your placement ID

        String placementName = String.format("projects/%s/locations/%s/catalogs/%s/eventStores/%s/placements/%s",
                projectId, location, catalogId, eventStoreId, placementId);

        logger.debug("Generated placementName: {}", placementName);

        UserInfo userInfo = UserInfo.newBuilder()
                .setUserId(productId)
                .build();

        UserEvent userEvent = UserEvent.newBuilder()
                .setEventType("detail-page-view")
                .setUserInfo(userInfo)
                .build();

        PredictRequest predictRequest = PredictRequest.newBuilder()
                .setName(placementName)
                .setUserEvent(userEvent)
                .build();

        List<PredictResponse.PredictionResult> recommendations = new ArrayList<>();

        try {
            logger.info("Sending prediction request to Google Recommendations AI");
            PredictionServiceClient.PredictPagedResponse response = predictionServiceClient.predict(predictRequest);

            for (PredictResponse.PredictionResult result : response.iterateAll()) {
                logger.debug("Received prediction result: ID = {}", result.getId());
                recommendations.add(result);
            }

            logger.info("Prediction request completed successfully with {} recommendations.", recommendations.size());

        } catch (ApiException e) {
            logger.error("API Exception occurred while making predictions: {}", e.getStatusCode(), e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during prediction", e);
        }

        List<PredictResponse.PredictionResult> filteredRecommendations = filterLocationsByCapacity(recommendations, quantity);
        logger.info("Filtered recommendations to {} locations based on capacity.", filteredRecommendations.size());

        return filteredRecommendations;
    }

    private List<PredictResponse.PredictionResult> filterLocationsByCapacity(List<PredictResponse.PredictionResult> recommendations, int quantity) {
        List<PredictResponse.PredictionResult> validLocations = new ArrayList<>();

        for (PredictResponse.PredictionResult recommendation : recommendations) {
            String locationId = recommendation.getId();
            int availableCapacity = getAvailableCapacity(locationId);
            logger.debug("Checking capacity for location ID {}: availableCapacity = {}, requested quantity = {}", locationId, availableCapacity, quantity);

            if (availableCapacity >= quantity) {
                validLocations.add(recommendation);
            } else {
                logger.warn("Location ID {} does not have enough capacity. Required: {}, Available: {}", locationId, quantity, availableCapacity);
            }
        }
        return validLocations;
    }

    private int getAvailableCapacity(String locationId) {
        int maxCapacity = 100;
        int currentQuantity = 60;
        int availableCapacity = maxCapacity - currentQuantity;
        logger.debug("Calculated available capacity for location ID {}: {}", locationId, availableCapacity);
        return availableCapacity;
    }
}
