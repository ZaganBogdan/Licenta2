package com.example.authService.security.jwt;

import lombok.Data;

@Data
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String username;
}
