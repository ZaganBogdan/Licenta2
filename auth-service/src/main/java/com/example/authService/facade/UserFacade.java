package com.example.authService.facade;

import com.example.authService.client.SportPlannerService.SportPlannerService;
import com.example.authService.entities.User;
import com.example.authService.payload.LoginRequest;
import com.example.authService.payload.SignupRequest;
import com.example.authService.security.jwt.LoginResponse;
import com.example.authService.services.MailService;
import com.example.authService.services.PasswordRecoveryService;
import com.example.authService.services.UserService;
import com.example.authService.entities.PasswordRecovery;
import com.example.authService.payload.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private final UserService userService;
    private final MailService mailService;
    private final PasswordRecoveryService passwordRecoveryService;
    private final SportPlannerService sportPlannerService;

    public UserFacade(UserService userService, MailService mailService, PasswordRecoveryService passwordRecoveryService, SportPlannerService sportPlannerService) {
        this.userService = userService;
        this.mailService = mailService;
        this.passwordRecoveryService = passwordRecoveryService;
        this.sportPlannerService = sportPlannerService;
    }

    public User signupUser(SignupRequest signupRequest) {
        User user = userService.signup(signupRequest);
        sportPlannerService.addUser(user.getUsername());
        return user;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }



    public String refreshToken(String refreshToken) {
        String token =  userService.refreshAccessToken(refreshToken);
        System.out.println("Token nou "+token);
        return token;
    }

    public void forgotPassword(String email, String originUrl) {
        if (userService.forgotPassword(email) != null) {
            PasswordRecovery passwordRecovery = passwordRecoveryService.addPasswordRecovery(email);
            mailService.sendForgotMessage(email,passwordRecovery.getUuid(),originUrl);
        }
    }

    public String resetPassword(ResetPasswordRequest request, String token){
        PasswordRecovery passwordRecovery = passwordRecoveryService.getPasswordRecovery(token);
        userService.resetPassword(request, passwordRecovery.getEmail());
        passwordRecoveryService.removePasswordRecovery(passwordRecovery);
        return "Your password was successfully reset";
    }
//    public LoginResponse googleOAuth2Login(final String idToken){
//       return userService.googleOAuth2Login(idToken);
//    }
}
