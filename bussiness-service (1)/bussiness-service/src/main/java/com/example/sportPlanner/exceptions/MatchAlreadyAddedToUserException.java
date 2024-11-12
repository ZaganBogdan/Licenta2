package com.example.sportPlanner.exceptions;

public class MatchAlreadyAddedToUserException extends RuntimeException {
    public MatchAlreadyAddedToUserException(String message) {
        super(message);
    }
}