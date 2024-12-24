package com.example.handler;

import com.example.exception.DataFetchException;
import com.example.exception.InvalidRateException;
import com.example.exception.response.AdvancedErrorResponse;
import com.example.util.HttpStatusDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataFetchException.class)
    public ResponseEntity<AdvancedErrorResponse> handleDataFetchException(DataFetchException ex) {
        log.error("DataFetchException occurred: {}", ex.getMessage(), ex);
        AdvancedErrorResponse errorResponse = new AdvancedErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatusDescription.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRateException.class)
    public ResponseEntity<AdvancedErrorResponse> handleInvalidRateException(InvalidRateException ex) {
        log.error("InvalidRateException occurred: {}", ex.getMessage(), ex);
        AdvancedErrorResponse errorResponse = new AdvancedErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatusDescription.BAD_REQUEST,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
