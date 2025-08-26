package com.chaau568.flashcards.service;

import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.HybridPayload;
import com.chaau568.flashcards.entity.User;

public interface UserService {
    void createAccount(User newUser);

    void login(String username, String password);

    void deleteAccount(String userId);

    void changeUsername(String userId, String newUsername);

    void changePassword(String userIdd, String newPassword);

    User loadUserByUsername(String username);

    void addOwnerDeck(String userId, Deck newDeck);

    void deleteOwnerDeckId(String userId, String deckId);

    // boolean checkAuthentication(String userId, String UserPass);
}
