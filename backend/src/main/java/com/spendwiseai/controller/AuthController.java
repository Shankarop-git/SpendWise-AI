package com.spendwiseai.controller;

import com.spendwiseai.dto.AuthDto;
import com.spendwiseai.security.UserPrincipal;
import com.spendwiseai.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, and demo access")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthDto.AuthResponse> register(@RequestBody AuthDto.RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<AuthDto.AuthResponse> login(@RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    public ResponseEntity<AuthDto.UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(authService.getCurrentUser(principal.getEmail()));
    }

    @PostMapping("/demo")
    @Operation(summary = "Get or create demo account with sample data")
    public ResponseEntity<AuthDto.AuthResponse> demo() {
        return ResponseEntity.ok(authService.getDemoAccount());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout (client-side token invalidation)")
    public ResponseEntity<Void> logout() {
        // JWT is stateless — client discards token
        return ResponseEntity.ok().build();
    }
}
