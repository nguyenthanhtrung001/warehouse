package com.example.mlservice.service;

import com.example.mlservice.dto.ForecastResult;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public interface IBigQueryService {

    public String testConnection();
    public List<ForecastResult> getForecastResults();
}
