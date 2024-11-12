package com.example.sportPlanner.controllers;

import com.example.sportPlanner.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class, MatchNotFoundException.class, LocationNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + ex.getMessage());
    }

    @ExceptionHandler({IncorrectDateException.class, InvalidPriceException.class})
    public ResponseEntity<String> handleInvalidInputsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + ex.getMessage());
    }
}
