package com.example.exception;

public class InvalidRateException extends RuntimeException {
    public InvalidRateException(String message) {
        super(message);
    }
}
