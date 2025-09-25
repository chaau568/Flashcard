package com.chaau568.flashcards.service;

import java.util.List;

import com.chaau568.flashcards.datatype.DeckInfo;
import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.Deck;

public interface DeckService {
    DeckInfo getDeckInfo(String deckId);

    String createDeck(String username, Deck newDeck); //

    void deleteDeck(String ownerUserId, String deckId);

    void updateDeck(String ownerUserId, String ownerDeckId, Deck updateDeck); //

    void addCard(String ownerUserId, String deckId, Card newCard); //

    void deletePrivateDecksByOwnerId(String ownerUserId);

    List<Deck> loadAllDecksFromOwnerUserId(String ownerUserId); //

    List<Deck> loadAllDecksFromPublicUserId(); //

}
