package com.devteria.identity.repository.httpclient;

import com.devteria.identity.configuration.AuthenticationRequestInterceptor;
import com.devteria.identity.dto.request.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devteria.identity.dto.request.ProfileCreationRequest;
import com.devteria.identity.dto.response.UserProfileResponse;

import java.util.List;

@FeignClient(name = "employee-service", url = "http://localhost:8085/api/employees",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PutMapping(value = "/update-account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUser(@PathVariable Long id);
    @GetMapping(value = "/warehouse/{warehouseId}/accountIds", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> listUserId(@PathVariable Long warehouseId);

}
