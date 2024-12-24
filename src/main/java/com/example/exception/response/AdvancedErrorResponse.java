package com.example.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class AdvancedErrorResponse {
    private int statusCode;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
}