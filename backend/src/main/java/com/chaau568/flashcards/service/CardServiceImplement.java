package com.chaau568.flashcards.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.exception.CardDontHavePermissionException;
import com.chaau568.flashcards.exception.CardNotFoundException;
import com.chaau568.flashcards.repository.CardRepository;

@Service

public class CardServiceImplement implements CardService {

    public final CardRepository cardRepository;

    public CardServiceImplement(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    private static final Map<String, Integer> progressMap = Map.of(
            "easy", 1,
            "normal", 0,
            "hard", -1,
            "again", -4);

    private static final Map<Integer, String> stateMap = Map.of(
            0, "new",
            1, "learning",
            2, "learning",
            3, "learning",
            4, "understanded");

    private static final Map<Integer, Integer> processingTimeMap = Map.of(
            0, 0,
            1, 1,
            2, 10,
            3, 60,
            4, 4320);

    public int getProgressValue(String progress) {
        Integer value = progressMap.get(progress);
        if (value == null) {
            throw new IllegalArgumentException("Unknown progress: " + progress);
        }
        return value;
    }

    public String getStateValue(Integer progress) {
        String value = stateMap.get(progress);
        if (value == null) {
            throw new IllegalArgumentException("Unknown progress: " + progress);
        }
        return value;
    }

    public int getProcessingTimeValue(Integer progress) {
        Integer value = processingTimeMap.get(progress);
        if (value == null) {
            throw new IllegalArgumentException("Unknown progress: " + progress);
        }
        return value;
    }

    public LocalDateTime calculateTime(Integer pushTime) {
        LocalDateTime time = LocalDateTime.now();
        time = time.plusMinutes(pushTime);
        return time;
    }

    @Override
    public String createCard(String ownerDeckId, Card newCard) {
        newCard.assignToDeck(ownerDeckId);
        newCard.setProgress(0);
        newCard.setState("new");
        newCard.setProcessingTime(calculateTime(0));
        return cardRepository.save(newCard).getId();
    }

    @Override
    public void deleteCard(String ownerDeckId, String cardId) {
        cardRepository.findById(cardId).ifPresentOrElse(cardOptional -> {
            if (cardOptional.getOwnerDeckId().equals(ownerDeckId)) {
                cardRepository.deleteById(cardOptional.getId());
            } else {
                throw new CardDontHavePermissionException("You are not the owner of Card with ID '" + cardId + "'.");
            }
        }, () -> {
            throw new CardNotFoundException("Card with ID '" + cardId + "' is not in deck '" + ownerDeckId + "'");
        });
    }

    @Override
    public void editCard(String ownerDeckId, String cardId, Card card) {
        cardRepository.findById(cardId).ifPresentOrElse(cardOptional -> {
            if (cardOptional.getOwnerDeckId().equals(ownerDeckId)) {
                cardOptional.setFrontCard(card.getFrontCard());
                cardOptional.setBackCard(card.getBackCard());
                cardRepository.save(cardOptional);
            } else {
                throw new CardDontHavePermissionException("You are not the owner of Card with ID '" + cardId + "'.");
            }
        }, () -> {
            throw new CardNotFoundException("Card with ID '" + cardId + "' is not in deck '" + ownerDeckId + "'");
        });
    }

    @Override
    @Transactional
    public void setTrackProgress(String ownerDeckId, String cardId, String progress) {
        cardRepository.findById(cardId).ifPresentOrElse(cardOptional -> {
            if (cardOptional.getOwnerDeckId().equals(ownerDeckId)) {
                int progressNum = getProgressValue(progress);
                int currentProgress = cardOptional.getProgress();
                if (currentProgress + progressNum > 0 && currentProgress + progressNum <= 4) {
                    currentProgress += progressNum;
                } else if (currentProgress + progressNum <= 1) {
                    currentProgress = 1;
                } else {
                    currentProgress = 4;
                }
                String currentState = getStateValue(currentProgress);
                int pushTime = getProcessingTimeValue(currentProgress);
                cardOptional.setProgress(currentProgress);
                cardOptional.setState(currentState);
                cardOptional.setProcessingTime(calculateTime(pushTime));
                cardRepository.save(cardOptional);
            } else {
                throw new CardDontHavePermissionException("You are not the owner of Card with ID '" + cardId + "'.");
            }
        }, () -> {
            throw new CardNotFoundException("Card with ID '" + cardId + "' is not in deck '" + ownerDeckId + "'");
        });
    }

    @Override
    @Transactional
    public List<Card> loadAllCardsFromOwnerDeckId(String deckId) {
        List<Card> cardListOption = cardRepository.findAllByOwnerDeckId(deckId);
        List<Card> loadAllCards = new ArrayList<>();
        LocalDateTime nowDateTime = LocalDateTime.now();
        for (Card card : cardListOption) {
            if (nowDateTime.isAfter(card.getProcessingTime())) {
                loadAllCards.add(card);
            }
        }
        return loadAllCards;
    }

}