package com.example.sportPlanner.exceptions;

public class UserAlreadyAddedToMatchException extends RuntimeException {
    public UserAlreadyAddedToMatchException(String message) {
        super(message);
    }
}