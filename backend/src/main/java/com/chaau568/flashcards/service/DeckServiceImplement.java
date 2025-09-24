package com.chaau568.flashcards.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.exception.DeckNotFoundException;
import com.chaau568.flashcards.exception.UserDontHavePermissionException;
import com.chaau568.flashcards.exception.UserNotFoundException;
import com.chaau568.flashcards.repository.DeckRepository;
import com.chaau568.flashcards.repository.UserRepository;

@Service

public class DeckServiceImplement implements DeckService {

    private final DeckRepository deckRepository;
    private final CardService cardService;
    private final UserRepository userRepository;

    public DeckServiceImplement(DeckRepository deckRepository, CardService cardService, UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String createDeck(String userId, Deck newDeck) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + userId + "' was not found."));
        String ownerUserId = user.getId();
        newDeck.assignToOwnerUserId(ownerUserId);
        return deckRepository.save(newDeck).getId();
    }

    @Override
    public void deleteDeck(String ownerUserId, String deckId) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck deckDelete = deckOptional.orElseThrow(() -> new DeckNotFoundException(
                "Deck with ID '" + deckId + "' was not found."));
        if (deckDelete.getOwnerUserId().equals(ownerUserId)) {
            deckRepository.deleteById(deckDelete.getId());
        } else {
            throw new UserDontHavePermissionException("You are not the owner of Deck with ID '" + deckId + "'.");
        }
    }

    @Override
    public void addCard(String ownerUserId, String deckId, Card newCard) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional
                .orElseThrow(() -> new DeckNotFoundException("Deck with ID '" + deckId + "' was not found."));
        if (!currentDeck.getOwnerUserId().equals(ownerUserId)) {
            throw new UserDontHavePermissionException("User with ID '" + ownerUserId + "' was not permission in this deck.");
        }
        List<String> cardListId = currentDeck.getCardListId();
        if (cardListId == null) {
            cardListId = new ArrayList<>();
            currentDeck.setCardListId(cardListId);
        }
        String cardId = cardService.createCard(currentDeck.getId(), newCard);
        cardListId.add(cardId);
        deckRepository.save(currentDeck);
    }

    @Override
    public void addTags(String ownerUserId, String deckId, List<String> tags) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional
                .orElseThrow(() -> new DeckNotFoundException("Deck with ID '" + deckId + "' was not found."));
        List<String> tagList = currentDeck.getTagList();
        if (tagList == null) {
            tagList = new ArrayList<>();
            currentDeck.setTagList(tagList);
        }
        for (String tag : tags) {
            if (!tagList.contains(tag)) {
                tagList.add(tag);
            }
        }
        deckRepository.save(currentDeck);
    }

    @Override
    @Transactional
    public void delectCard(String ownerUserId, String deckId, String cardId) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional
                .orElseThrow(() -> new DeckNotFoundException("Deck with ID '" + deckId + "' was not found."));
        cardService.deleteCard(cardId, deckId);
        List<String> cardListId = currentDeck.getCardListId();
        if (!cardListId.isEmpty() && cardListId.contains(cardId)) {
            cardListId.remove(cardId);
        }
        deckRepository.save(currentDeck);
    }

    @Override
    public void delectTags(String ownerUserId, String deckId, List<String> tags) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional
                .orElseThrow(() -> new DeckNotFoundException("Deck with ID '" + deckId + "' was not found."));
        List<String> tagList = currentDeck.getTagList();
        if (!tagList.isEmpty()) {
            for (String tag : tagList) {
                tagList.remove(tag);
            }
            deckRepository.save(currentDeck);
        }
    }

    @Override
    public void editCard(String ownerUserId, String deckId, String cardId, Card card) {
        deckRepository.findById(deckId).orElseThrow(() -> new DeckNotFoundException(
                "Deck with ID '" + deckId + "' was not found."));
        cardService.editCard(deckId, cardId, card);
    }

    @Override
    public void deletePrivateDecksByOwnerId(String ownerUserId) {
        List<Deck> deckList = deckRepository.findAllByOwnerUserId(ownerUserId);
        for (Deck currentDeck : deckList) {
            if (!currentDeck.getIsPublic()) {
                deckRepository.deleteById(currentDeck.getId()); // delete deck when deck=>isPublic is false
            }
        }
    }

    @Override
    public Deck loadDeckById(String deckId) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional.orElseThrow(() -> new DeckNotFoundException(
                "Deck with ID '" + deckId + "' was not found."));
        return currentDeck;
    }

    @Override
    public List<Deck> loadAllDecksFromOwnerUserId(String ownerUserId) {
        List<Deck> deckList = deckRepository.findAllByOwnerUserId(ownerUserId);
        userRepository.findById(ownerUserId).orElseThrow(() -> {
            throw new UserNotFoundException("User with ID '" + ownerUserId + "' was not found.");
        });
        return deckList;
    }

    @Override
    public List<Deck> loadAllDecksFromPublicUserId() {
        List<Deck> deckList = deckRepository.findAllByIsPublicTrue();
        return deckList;
    }

}
