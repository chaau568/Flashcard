package com.chaau568.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chaau568.flashcards.response.ApiResponse;

@RestControllerAdvice

public class CardException {
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCardNotFound(CardNotFoundException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
