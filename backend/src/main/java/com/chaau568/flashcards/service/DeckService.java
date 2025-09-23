package com.chaau568.flashcards.service;

import java.util.List;

import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.Deck;

public interface DeckService {
    String createDeck(String username, Deck newDeck);

    void deleteDeck(String ownerUserId, String deckId);

    void addCard(String ownerUserId, String deckId, Card newCard);

    void addTags(String ownerUserId, String deckId, List<String> tags);

    void delectCard(String ownerUserId, String deckId, String cardId);

    void delectTags(String ownerUserId, String deckId, List<String> tags);

    void editCard(String ownerUserId, String deckId, String cardId, Card card);

    void deletePrivateDecksByOwnerId(String ownerUserId);

    Deck loadDeckById(String deckId);

    List<Deck> loadAllDecksFromOwnerUserId(String ownerUserId);

    List<Deck> loadAllDecksFromPublicUserId();

}
