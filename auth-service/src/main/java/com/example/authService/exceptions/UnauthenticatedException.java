package com.example.authService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class UnauthenticatedException extends ResponseStatusException {

    public UnauthenticatedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
    public UnauthenticatedException( String reason) {
        this(HttpStatus.UNAUTHORIZED,reason);
    }
}