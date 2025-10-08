package com.chaau568.flashcards.service;

import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.User;

public interface UserService {
    void createAccount(User newUser);

    void login(String username, String password);

    void deleteAccount(String userId);

    void changeUsername(String userId, String newUsername);

    void changePassword(String userId, String newPassword);

    User loadUserByUsername(String username);

    // String addOwnerDeck(String userId, Deck newDeck);

    // void deleteOwnerDeckId(String userId, String deckId);

    String getUserId(String username);

    // boolean checkAuthentication(String userId, String UserPass);
}
