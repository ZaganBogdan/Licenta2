package com.example.authService.services;

import com.example.authService.entities.User;
import com.example.authService.exceptions.*;
import com.example.authService.payload.SignupRequest;
import com.example.authService.repositories.UserRepository;
import com.example.authService.entities.Type;
import com.example.authService.payload.LoginRequest;
import com.example.authService.payload.ResetPasswordRequest;
import com.example.authService.security.jwt.JwtUtils;
import com.example.authService.security.jwt.LoginResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import dev.samstevens.totp.code.CodeVerifier;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final GoogleIdTokenVerifier googleVerifier;
    private final CodeVerifier codeVerifier;

    public UserService(final UserRepository userRepository, final PasswordEncoder encoder, final JwtUtils jwtUtils, GoogleIdTokenVerifier googleVerifier, CodeVerifier codeVerifier) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.googleVerifier = googleVerifier;
        this.codeVerifier = codeVerifier;
    }

    public User signup(final SignupRequest signupRequest) {
        if (userRepository.findUserByUsername(signupRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + signupRequest.getUsername() + " is already used!");
        }
        if (userRepository.findUserByEmail(signupRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("Email " + signupRequest.getEmail() + " is already used!");
        }
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword()))
            throw new PasswordException("Passwords didn't match");

        return createUser(signupRequest);
    }

    public LoginResponse loginUser(final LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new PasswordException("Incorrect password");
        }
        return new LoginResponse(jwtUtils.generateAccessJwtToken(user), jwtUtils.generateRefreshJwtToken(user),user.getUsername());
    }

    private User createUser(final SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setType(signupRequest.getType());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        return userRepository.save(user);
    }

    public User getUserFromAccessToken(final String token) {
        return userRepository.findUserByUsername(jwtUtils.getUsernameFromJwtAccessToken(token))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public String refreshAccessToken(final String refreshToken) {
        User user = userRepository.findUserByUsername(jwtUtils.getUsernameFromJwtRefreshToken(refreshToken))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return jwtUtils.generateAccessJwtToken(user);
    }

    public User forgotPassword(final String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

    }

    public void resetPassword(ResetPasswordRequest request, String email) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordException("Password do not match");
        }
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
    }

//    @SneakyThrows
//    public LoginResponse googleOAuth2Login(String idTokenString) {
//        GoogleIdToken idToken = googleVerifier.verify(idTokenString);
//
//        if (idToken == null) {
//            throw new InvalidTokenException("Invalid Token");
//        }
//
//        Payload payload = idToken.getPayload();
//        String email = payload.getEmail();
//
//        User user = userRepository.findUserByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        if (user==null) {
//            user.setType(Type.USER);
//            user.setPassword(encoder.encode(""));
//            try {
//                user = userRepository.save(user);
//            } catch (EmailAlreadyUsedException exception) {
//                throw new EmailAlreadyUsedException("Email already used");
//            }
//        }

//     return new LoginResponse(jwtUtils.generateAccessJwtToken(user),
//             jwtUtils.generateRefreshJwtToken(user));
//
//    }
}
