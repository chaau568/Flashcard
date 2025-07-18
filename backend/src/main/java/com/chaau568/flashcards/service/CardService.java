package com.chaau568.flashcards.service;

import java.util.List;

import com.chaau568.flashcards.entity.Card;

public interface CardService {
    String createCard(String ownerDeckId, Card newCard);

    void deleteCard(String ownerDeckId, String cardId);

    void editCard(String ownerDeckId, String cardId, Card card);

    void setTrackProgress(String ownerDeckId, String cardId, String progress);

    List<Card> loadAllCardsFromOwnerDeckId(String deckId);
}
