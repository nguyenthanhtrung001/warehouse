package com.example.mlservice.service.impl;

import com.example.mlservice.service.IVertexAiPredictionService;
import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictRequest;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.*;
import com.google.protobuf.Value;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class implVertexAiPredictionService implements IVertexAiPredictionService {
    private static final String PROJECT_ID = "warehouse-439807";
    private static final String LOCATION = "us-central1"; // Đảm bảo khớp với vị trí đã triển khai mô hình
    private static final String ENDPOINT_ID = "your-endpoint-id"; // ID của Endpoint Vertex AI đã triển khai

    public List<Value> predict(List<String> featureData) throws IOException {
        try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create()) {
            EndpointName endpointName = EndpointName.of(PROJECT_ID, LOCATION, ENDPOINT_ID);

            // Tạo request với dữ liệu đầu vào
            PredictRequest predictRequest = PredictRequest.newBuilder()
                    .setEndpoint(endpointName.toString())
                    .addAllInstances(featureData.stream()
                            .map(f -> Value.newBuilder().setStringValue(f).build())
                            .collect(Collectors.toList()))
                    .build();

            // Gửi request tới Vertex AI để dự đoán
            return predictionServiceClient.predict(predictRequest)
                    .getPredictionsList();
        }
    }
}
