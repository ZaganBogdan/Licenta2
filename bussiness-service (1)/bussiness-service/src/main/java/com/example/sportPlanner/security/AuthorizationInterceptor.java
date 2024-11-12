package com.example.sportPlanner.security;

import com.example.sportPlanner.exceptions.NoBearerTokenException;
import com.example.sportPlanner.exceptions.UnauthenticatedException;
import com.example.sportPlanner.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthorizationInterceptor(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new UnauthenticatedException(authorizationHeader);
        }
        if(!authorizationHeader.startsWith("Bearer ")) {
            throw new NoBearerTokenException("Invalid token");
        }
        if(!jwtUtils.validateJwtToken(authorizationHeader.substring(7))){
            throw new UnauthenticatedException(authorizationHeader);
        }
      //  request.setAttribute("user", userService.getUserFromAccessToken(authorizationHeader.substring(7)));
        return true;
    }
}
