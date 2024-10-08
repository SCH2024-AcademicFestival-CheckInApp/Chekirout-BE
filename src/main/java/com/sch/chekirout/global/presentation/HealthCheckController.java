package com.sch.chekirout.global.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Server is healthy");
    }
}
