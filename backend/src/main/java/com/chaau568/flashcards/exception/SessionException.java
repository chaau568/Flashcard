package com.chaau568.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chaau568.flashcards.response.ApiResponse;

@RestControllerAdvice

public class SessionException {
    @ExceptionHandler()
    public ResponseEntity<ApiResponse> handleSessionNotFound(SessionNotFound ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
