package com.student.performance.controller;

import com.student.performance.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> index() {
        return ResponseEntity.ok(ApiResponse.success(
            Map.of("message", "Student Performance Analytics API", "status", "running"),
            "API is running successfully"
        ));
    }
    
    @GetMapping("/api/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success(
            Map.of("status", "UP", "timestamp", java.time.LocalDateTime.now().toString()),
            "Service is healthy"
        ));
    }
}
