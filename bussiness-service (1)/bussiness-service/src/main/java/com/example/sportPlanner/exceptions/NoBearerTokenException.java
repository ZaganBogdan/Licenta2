package com.example.sportPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoBearerTokenException extends ResponseStatusException {

    public NoBearerTokenException( String reason) {
        super(HttpStatus.UNAUTHORIZED,reason);
    }
}

