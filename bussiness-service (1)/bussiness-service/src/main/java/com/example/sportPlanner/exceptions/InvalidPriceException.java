package com.example.sportPlanner.exceptions;

public class InvalidPriceException extends RuntimeException{
    public InvalidPriceException(String message) {
        super(message);
    }

}