package com.example.employeeservice.client;



import com.example.employeeservice.dto.request.ApiResponse;
import com.example.employeeservice.dto.request.UserCreationRequest;
import com.example.employeeservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "identity-service", url = "http://identity/identity")
public interface IdentityClient {
    @PostMapping(value = "/users/createEmployee",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request);
    @DeleteMapping(value = "/users/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> deleteUserByUsername(@PathVariable String username);

}
