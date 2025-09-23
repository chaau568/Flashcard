package com.chaau568.flashcards.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.exception.PasswordWrongException;
import com.chaau568.flashcards.exception.UserNotFoundException;
import com.chaau568.flashcards.exception.UsernameAlreadyExistsException;
import com.chaau568.flashcards.repository.UserRepository;

@Service

public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final DeckService deckService;

    public UserServiceImplement(UserRepository usersRepository, DeckService deckService) {
        this.userRepository = usersRepository;
        this.deckService = deckService;
    }

    @Override
    public void createAccount(User newUser) {
        userRepository.findByUsername(newUser.getUsername()).ifPresentOrElse(
                userExists -> {
                    throw new UsernameAlreadyExistsException(
                            "Username '" + userExists.getUsername() + "' already exists.");
                }, () -> {
                    userRepository.save(newUser);
                });
    }

    @Override
    public void login(String username, String password) {
        userRepository.findByUsername(username).ifPresentOrElse(
                userExists -> {
                    if (!userExists.getPassword().equals(password)) {
                        throw new PasswordWrongException(
                                "Password '" + password + "' was incorrect.");
                    }
                }, () -> {
                    throw new UserNotFoundException(
                            "User with name '" + username + "' was not found.");
                });
    }

    @Override
    @Transactional
    public void deleteAccount(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User userDelete = userOptional.orElseThrow(
                () -> new UserNotFoundException("User with ID '" + userId + "' was not found."));
        deckService.deletePrivateDecksByOwnerId(userDelete.getId());
        userRepository.deleteById(userDelete.getId());
    }

    @Override
    @Transactional
    public void changeUsername(String userId, String newUsername) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            User editUsername = user;
            editUsername.setUsername(newUsername);
            userRepository.save(editUsername);
        }, () -> {
            throw new UserNotFoundException("User with ID '" + userId + "' was not found.");
        });
    }

    @Override
    @Transactional
    public void changePassword(String userId, String newPassword) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            User editUserpass = user;
            editUserpass.setPassword(newPassword);
            userRepository.save(editUserpass);
        }, () -> {
            throw new UserNotFoundException("User with ID '" + userId + "' was not found.");
        });
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with NAME '" + username + "' was not found."));
    }

    @Override
    @Transactional
    public void addOwnerDeck(String userId, Deck newDeck) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            User currentUser = user;
            List<String> ownerDeckId = currentUser.getOwnerDeckIds();
            if (ownerDeckId == null || ownerDeckId.isEmpty()) {
                ownerDeckId = new ArrayList<>();
                currentUser.setOwnerDeckIds(ownerDeckId);
            }
            String deckId = deckService.createDeck(userId, newDeck);
            ownerDeckId.add(deckId);
            userRepository.save(currentUser);
        }, () -> {
            throw new UserNotFoundException("User with ID '" + userId + "' was not found.");
        });
    }

    @Override
    @Transactional
    public void deleteOwnerDeckId(String userId, String deckId) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            User currentUser = user;
            deckService.deleteDeck(userId, deckId);
            List<String> deckIds = currentUser.getOwnerDeckIds();
            if (!deckId.isEmpty()) {
                deckIds.remove(deckId);
            }
            userRepository.save(currentUser);
        }, () -> {
            throw new UserNotFoundException("User with ID '" + userId + "' was not found.");
        });
    }

    @Override
    public String getUserId(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UserNotFoundException("User with NAME '" + username + "' was not found.");
        });
        String userId = user.getId();
        return userId;
    }

}
