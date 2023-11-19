package com.gym.domain.exception;

public class LogInFailedException extends RuntimeException{
    public LogInFailedException(String message) {
        super(message);
    }
}
