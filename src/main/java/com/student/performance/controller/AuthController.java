package com.student.performance.controller;

import com.student.performance.dto.ApiResponse;
import com.student.performance.dto.LoginRequest;
import com.student.performance.dto.LoginResponse;
import com.student.performance.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }
}
