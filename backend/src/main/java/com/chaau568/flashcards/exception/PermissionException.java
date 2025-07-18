package com.chaau568.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chaau568.flashcards.response.ApiResponse;

@RestControllerAdvice

public class PermissionException {
    @ExceptionHandler(UserDontHavePermissionException.class)
    public ResponseEntity<ApiResponse> handleUserDontHavePermission(UserDontHavePermissionException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DeckDontHavePermissionException.class)
    public ResponseEntity<ApiResponse> handleDeckDontHavePermission(DeckDontHavePermissionException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CardDontHavePermissionException.class)
    public ResponseEntity<ApiResponse> handleCardDontHavePermission(CardDontHavePermissionException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }
    
}
