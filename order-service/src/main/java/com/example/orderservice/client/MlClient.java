package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ml-service", url = "http://localhost:9999")
public interface MlClient {
    @PostMapping(value = "/api/bigquery/csv/upload", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> uploadFile(@RequestParam("filePath") String filePath);

}
