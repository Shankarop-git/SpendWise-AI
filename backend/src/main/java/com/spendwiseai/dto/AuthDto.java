package com.spendwiseai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class AuthResponse {
        private String token;
        private UserResponse user;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private boolean isDemo;
    }
}
