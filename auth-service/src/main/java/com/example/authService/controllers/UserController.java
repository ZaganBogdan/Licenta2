package com.example.authService.controllers;

import com.example.authService.entities.User;
import com.example.authService.facade.UserFacade;
import com.example.authService.payload.LoginRequest;
import com.example.authService.payload.ResetPasswordRequest;
import com.example.authService.payload.SignupRequest;
import com.example.authService.security.jwt.LoginResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user/")
public class UserController {
    private final UserFacade userFacade;


    public UserController(final UserFacade userFacade) {
        this.userFacade = userFacade;

    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody final SignupRequest signUpRequest) {
        User user = userFacade.signupUser(signUpRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginRequest logInRequest, HttpServletResponse httpServletResponse) {
        LoginResponse loginResponse = userFacade.loginUser(logInRequest);
        Cookie cookie = new Cookie("refresh_token", loginResponse.getRefreshToken());
        cookie.setMaxAge(36000);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/authenticatedUser")
    public ResponseEntity<?> authenticatedUser(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> resfreshToken(@CookieValue("refresh_token") String refreshToken) {
        System.out.println(refreshToken);
        return ResponseEntity.ok(userFacade.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Succesfully logout!");
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable final String email, HttpServletRequest request) {
        String originUrl = request.getHeader("Origin");
        userFacade.forgotPassword(email, originUrl);
        return ResponseEntity.ok("Check your email");
    }

    @PostMapping(value = "/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, @PathVariable(value = "token") String token) {
        return ResponseEntity.ok(userFacade.resetPassword(request, token));
    }

//    @PostMapping(value = "/google-oauth2")
//    public ResponseEntity<?> googleOAuth2(@RequestBody @JsonProperty("token") String idToken, HttpServletResponse response) {
//        LoginResponse loginResponse = userFacade.googleOAuth2Login(idToken);
//        Cookie cookie = new Cookie("refresh_token", loginResponse.getRefreshToken());
//        cookie.setMaxAge(36000);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//
//        response.addCookie(cookie);
//
//        return ResponseEntity.ok(loginResponse.getAccessToken());
//    }

}
