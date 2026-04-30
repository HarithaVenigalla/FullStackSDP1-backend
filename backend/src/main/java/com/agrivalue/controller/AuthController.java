package com.agrivalue.controller;

import com.agrivalue.dto.AuthResponse;
import com.agrivalue.dto.LoginRequest;
import com.agrivalue.dto.RegisterRequest;
import com.agrivalue.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-request")
    public ResponseEntity<String> registerRequest(@RequestBody RegisterRequest request) {
        authService.initiateRegistration(request);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody com.agrivalue.dto.OtpVerificationRequest request) {
        return ResponseEntity.ok(authService.verifyAndRegister(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
