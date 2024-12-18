package com.example.mlservice.dto;


public class ForecastResult {
    private String itemId;
    private String forecastTimestamp;
    private String forecastValue;
    private String standardError;
    private String confidenceLevel;
    private String predictionIntervalLowerBound;
    private String predictionIntervalUpperBound;
    private String confidenceIntervalLowerBound;
    private String confidenceIntervalUpperBound;

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getForecastTimestamp() {
        return forecastTimestamp;
    }

    public void setForecastTimestamp(String forecastTimestamp) {
        this.forecastTimestamp = forecastTimestamp;
    }

    public String getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(String forecastValue) {
        this.forecastValue = forecastValue;
    }

    public String getStandardError() {
        return standardError;
    }

    public void setStandardError(String standardError) {
        this.standardError = standardError;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getPredictionIntervalLowerBound() {
        return predictionIntervalLowerBound;
    }

    public void setPredictionIntervalLowerBound(String predictionIntervalLowerBound) {
        this.predictionIntervalLowerBound = predictionIntervalLowerBound;
    }

    public String getPredictionIntervalUpperBound() {
        return predictionIntervalUpperBound;
    }

    public void setPredictionIntervalUpperBound(String predictionIntervalUpperBound) {
        this.predictionIntervalUpperBound = predictionIntervalUpperBound;
    }

    public String getConfidenceIntervalLowerBound() {
        return confidenceIntervalLowerBound;
    }

    public void setConfidenceIntervalLowerBound(String confidenceIntervalLowerBound) {
        this.confidenceIntervalLowerBound = confidenceIntervalLowerBound;
    }

    public String getConfidenceIntervalUpperBound() {
        return confidenceIntervalUpperBound;
    }

    public void setConfidenceIntervalUpperBound(String confidenceIntervalUpperBound) {
        this.confidenceIntervalUpperBound = confidenceIntervalUpperBound;
    }
}
