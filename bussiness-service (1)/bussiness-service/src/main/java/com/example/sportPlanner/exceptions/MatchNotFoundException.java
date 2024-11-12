package com.example.sportPlanner.exceptions;

public class MatchNotFoundException extends RuntimeException{
    public MatchNotFoundException(String message) {
        super(message);
    }
}
