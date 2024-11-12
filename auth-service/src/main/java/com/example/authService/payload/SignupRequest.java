package com.example.authService.payload;

import com.example.authService.entities.Type;
import lombok.*;

@NoArgsConstructor

@AllArgsConstructor
@Data
public class SignupRequest {

    private String email;
    private Type type;
    private String username;
    private String password;
    private String confirmPassword;

}
