package com.example.sportPlanner.exceptions;

public class IncorrectDateException extends  RuntimeException{
    public IncorrectDateException(String message) {
        super(message);
    }
}
