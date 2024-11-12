package com.example.sportPlanner.exceptions;

public class FriendsNotFoundException extends RuntimeException {
    public FriendsNotFoundException(String message) {
        super(message);
    }
}