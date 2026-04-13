package com.devteria.identity.controller;

import java.util.List;

import com.devteria.identity.dto.request.*;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.devteria.identity.dto.response.UserResponse;
import com.devteria.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    @PostMapping("/createEmployee")
    ApiResponse<UserResponse> createEmployee(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createEmployee(request))
                .build();
    }
    @PostMapping("/createEmployeeAuto")
    ApiResponse<UserResponse> createEmployee(@RequestParam @Valid Long employeeId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createEmployeeAuto(employeeId))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    @GetMapping("/employee/warehouse/{warehouseId}")
    ApiResponse<List<UserResponse>> getUsersEmployee(@PathVariable("warehouseId") Long warehouseId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsersEmployee(warehouseId))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }
    @DeleteMapping("/{username}")
    public ApiResponse<String> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ApiResponse.<String>builder()
                .result("User with username '" + username + "' has been deleted")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
    @PutMapping("/{userId}/reset-password")
    public ApiResponse<String> resetPassword(@PathVariable String userId, @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(userId, request.getNewPassword());
        return ApiResponse.<String>builder()
                .result("Password reset successfully")
                .build();
    }

    @PutMapping("/{userId}/lock")
    public ApiResponse<String> lockUser(@PathVariable String userId) {
        userService.lockUser(userId);
        return ApiResponse.<String>builder()
                .result("User account locked successfully")
                .build();
    }

    // API để mở tài khoản
    @PutMapping("/{userId}/unlock")
    public ApiResponse<String> unlockUser(@PathVariable String userId) {
        userService.unlockUser(userId);
        return ApiResponse.<String>builder()
                .result("User account unlocked successfully")
                .build();
    }
    // API cập nhật quyền.
    @PutMapping("/{userId}/roles")
    public ApiResponse<String> updateUserRoles(@PathVariable String userId, @RequestBody UpdateUserRolesRequest request) {
        try {
            userService.updateUserRoles(userId, request.getRoles());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return ApiResponse.<String>builder()
                .result("User roles updated successfully")
                .build();
    }



}
