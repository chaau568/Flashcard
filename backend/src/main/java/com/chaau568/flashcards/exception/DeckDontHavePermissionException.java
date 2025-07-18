package com.chaau568.flashcards.exception;

public class DeckDontHavePermissionException extends RuntimeException{
    public DeckDontHavePermissionException(String message) {
        super(message);
    }
}
