package com.example.mlservice.service;

import com.google.protobuf.Value;

import java.io.IOException;
import java.util.List;
public interface IVertexAiPredictionService {
    public List<Value> predict(List<String> featureData) throws IOException;
}
