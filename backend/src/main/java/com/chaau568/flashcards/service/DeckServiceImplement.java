package com.chaau568.flashcards.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaau568.flashcards.datatype.DeckInfo;
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
    public DeckInfo getDeckInfo(String deckId) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck deck = deckOptional.orElseThrow(() -> new DeckNotFoundException(
                "Deck with ID '" + deckId + "' was not found."));

        String ownerUserId = deck.getOwnerUserId();
        Optional<User> user = userRepository.findById(ownerUserId);
        User ownerUser = user
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + ownerUserId + "' was not found."));
        String ownerUserName = ownerUser.getUsername();

        DeckInfo deckInfo = new DeckInfo();
        deckInfo.setOwnerUsername(ownerUserName);
        deckInfo.setIsPublic(deck.getIsPublic());
        deckInfo.setDeckName(deck.getDeckName());
        deckInfo.setTagList(deck.getTagList());
        deckInfo.setCreateAt(deck.getCreatedAt());
        deckInfo.setUpdateAt(deck.getUpdatedAt());

        return deckInfo;
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
    @Transactional
    public void updateDeck(String ownerUserId, String ownerDeckId, Deck updateDeck) {
        Optional<Deck> deckOptional = deckRepository.findById(ownerDeckId);
        Deck deckUpdate = deckOptional.orElseThrow(() -> new DeckNotFoundException(
                "Deck with ID '" + ownerDeckId + "' was not found."));
        if (!deckUpdate.getOwnerUserId().equals(ownerUserId)) {
            throw new UserDontHavePermissionException(
                    "You are not the owner of Deck with ID '" + deckUpdate.getId() + "'.");
        }

        deckUpdate.setDeckName(updateDeck.getDeckName());
        deckUpdate.setIsPublic(updateDeck.getIsPublic());
        deckUpdate.setTagList(updateDeck.getTagList());
        deckRepository.save(deckUpdate);
    }

    @Override
    public void addCard(String ownerUserId, String deckId, Card newCard) {
        Optional<Deck> deckOptional = deckRepository.findById(deckId);
        Deck currentDeck = deckOptional
                .orElseThrow(() -> new DeckNotFoundException("Deck with ID '" + deckId + "' was not found."));
        if (!currentDeck.getOwnerUserId().equals(ownerUserId)) {
            throw new UserDontHavePermissionException(
                    "User with ID '" + ownerUserId + "' was not permission in this deck.");
        }
        cardService.createCard(currentDeck.getId(), newCard);
        deckRepository.save(currentDeck);
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
