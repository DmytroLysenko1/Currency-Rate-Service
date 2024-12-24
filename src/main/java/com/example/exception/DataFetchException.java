package com.example.exception;

public class DataFetchException extends RuntimeException {
    public DataFetchException(String message) {
        super(message);
    }
}
