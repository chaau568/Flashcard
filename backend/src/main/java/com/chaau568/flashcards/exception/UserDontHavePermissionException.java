package com.chaau568.flashcards.exception;

public class UserDontHavePermissionException extends RuntimeException{
    public UserDontHavePermissionException(String message) {
        super(message);
    }
}
