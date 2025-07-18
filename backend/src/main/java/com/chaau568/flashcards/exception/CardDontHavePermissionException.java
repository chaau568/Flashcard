package com.chaau568.flashcards.exception;

public class CardDontHavePermissionException extends RuntimeException {
    public CardDontHavePermissionException(String message) {
        super(message);
    }
}
