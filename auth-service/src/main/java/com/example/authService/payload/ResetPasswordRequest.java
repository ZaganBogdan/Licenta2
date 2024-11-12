package com.example.authService.payload;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private final String password;
    private final String confirmPassword;
}
