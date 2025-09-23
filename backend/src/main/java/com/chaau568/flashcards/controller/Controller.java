package com.chaau568.flashcards.controller;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.boot.web.server.MimeMappings.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.CardProgressUpdate;
import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.UpdateCardForm;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.exception.SessionNotFound;
import com.chaau568.flashcards.repository.UserRepository;
import com.chaau568.flashcards.response.ApiResponse;
import com.chaau568.flashcards.response.ApiResponseWithData;
import com.chaau568.flashcards.service.CardService;
import com.chaau568.flashcards.service.DeckService;
import com.chaau568.flashcards.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:5137")
@RestController
@RequestMapping("/flashcard")

public class Controller {
    private final UserService userService;
    private final DeckService deckService;
    private final CardService cardService;

    public Controller(UserService userService, DeckService deckService, CardService cardService) {
        this.userService = userService;
        this.deckService = deckService;
        this.cardService = cardService;
    }

    // Checking Authen
    @GetMapping("/authen")
    public ResponseEntity<ApiResponse> authen(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            ApiResponse response = new ApiResponse("Not logged in yet.", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        ApiResponse response = new ApiResponse("Already logged in.", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // User
    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody User newUser) {
        userService.createAccount(newUser);
        ApiResponse response = new ApiResponse("User created successfully.", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Map<String, String> user, HttpServletRequest request) {
        String username = user.get("username");
        String password = user.get("password");
        String userId = userService.getUserId(username);
        userService.login(username, password);
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", userId);
        session.setAttribute("username", username);
        ApiResponse response = new ApiResponse("User logined successfully.", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse> logout(HttpSession session) {
        session.invalidate();
        ApiResponse response = new ApiResponse("User logouted successfully.", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PutMapping("/user/update/{id}/{username}/{password}")
    // public ResponseEntity<ApiResponse> updateUser(@PathVariable String id,
    // @PathVariable String username,
    // @PathVariable String password) {
    // userService.updateUser(id, username, password);
    // ApiResponse response = new ApiResponse("User updated successfully",
    // HttpStatus.OK.value());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @DeleteMapping("/user/delete/{id}")
    // public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
    // userService.deleteUser(id);
    // ApiResponse response = new ApiResponse("User deleted successfully",
    // HttpStatus.OK.value());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/user/get_by_name/{username}")
    public ResponseEntity<ApiResponseWithData> getUserByName(@PathVariable String username) {
        User user = userService.loadUserByUsername(username);
        ApiResponseWithData response = new ApiResponseWithData("User getted by username successfully.",
                HttpStatus.OK.value(), user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @GetMapping("/user/get_by_id/{id}")
    // public ResponseEntity<ApiResponseWithData> getUserById(@PathVariable String
    // id) {
    // User user = userService.getUserById(id);
    // ApiResponseWithData response = new ApiResponseWithData("User getted by id
    // successfully", HttpStatus.OK.value(),
    // user);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // Deck
    @PostMapping("/deck/create")
    public ResponseEntity<ApiResponse> createDeck(@RequestBody Deck newDeck, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        String userId = (String) session.getAttribute("userId");
        userService.addOwnerDeck(userId, newDeck); // สร้าง deckId ใน ownerUser ก่อน แล้วค่อยสร้าง deck ที่หลัง
        // deckService.createDeck(username, newDeck);
        ApiResponse response = new ApiResponse("Deck created successfully.",
                HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // @PutMapping("/deck/update/{id}/{userId}/{isPublic}")
    // public ResponseEntity<ApiResponse> updateDeck(@PathVariable String id,
    // @PathVariable String userId,
    // @PathVariable Boolean isPublic, @RequestBody List<String> cardListId) {
    // deckService.updateDeck(id, userId, cardListId, isPublic);
    // ApiResponse response = new ApiResponse("Deck updated successfully",
    // HttpStatus.OK.value());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @DeleteMapping("/deck/delete/{id}")
    // public ResponseEntity<ApiResponse> deleteDeck(@PathVariable String id) {
    // deckService.deleteDeck(id);
    // ApiResponse response = new ApiResponse("Deck deleted successfully",
    // HttpStatus.OK.value());
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @GetMapping("/deck/get_by_id/{id}")
    // public ResponseEntity<ApiResponseWithData> getDeckById(@PathVariable String
    // id) {
    // Deck deck = deckService.getById(id);
    // ApiResponseWithData response = new ApiResponseWithData("Deck getted by id
    // successfully", HttpStatus.OK.value(),
    // deck);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/deck/get_by_owner_user_id")
    public ResponseEntity<ApiResponseWithData> getDeckByOwnerUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        String ownerUserId = (String) session.getAttribute("userId");
        List<Deck> deckList = deckService.loadAllDecksFromOwnerUserId(ownerUserId);
        ApiResponseWithData response = new ApiResponseWithData("Deck getted by owner user id successfully.",
                HttpStatus.OK.value(), deckList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/deck/get_by_public")
    public ResponseEntity<ApiResponseWithData> getDeckByPublic(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        List<Deck> deckList = deckService.loadAllDecksFromPublicUserId();
        ApiResponseWithData response = new ApiResponseWithData("Deck getted by owner user id successfully.",
                HttpStatus.OK.value(), deckList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @GetMapping("/deck/get_by_tag/{tag}")
    // public ResponseEntity<ApiResponseWithData> getDeckByTag(@PathVariable String
    // tag) {
    // List<Deck> deckList = deckService.getAllByTagList(tag);
    // ApiResponseWithData response = new ApiResponseWithData("Deck getted by tag
    // successfully", HttpStatus.OK.value(),
    // deckList);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // @GetMapping("/deck/get_by_tags/{tags}")
    // public ResponseEntity<ApiResponseWithData> getDeckByTags(@PathVariable
    // List<String> tags) {
    // List<Deck> deckList = deckService.getAllByTageListIn(tags);
    // ApiResponseWithData response = new ApiResponseWithData("Deck getted by tag
    // successfully", HttpStatus.OK.value(),
    // deckList);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    // Card
    @GetMapping("/card/get_by_deck_id/{ownerId}")
    public ResponseEntity<ApiResponseWithData> getAllCardsByDeckId(HttpServletRequest request,
            @PathVariable String ownerId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        List<Card> deckList = cardService.loadAllCardsFromOwnerDeckId(ownerId);
        ApiResponseWithData response = new ApiResponseWithData("All Cards getted by owner deck id successfully.",
                HttpStatus.OK.value(), deckList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/card/create")
    public ResponseEntity<ApiResponse> createCard(HttpServletRequest request, @RequestBody Card newCard) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        String deckId = newCard.getOwnerDeckId();
        cardService.createCard(deckId, newCard);
        ApiResponse response = new ApiResponse("Card created successfully",
                HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/card/update_progress_card")
    public ResponseEntity<ApiResponse> updateProgressCard(HttpServletRequest request,
            @RequestBody UpdateCardForm cardForm) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String deckId = cardForm.getDeckId();
        for (CardProgressUpdate update : cardForm.getResults()) {
            String cardId = update.getCardId();
            String progress = update.getProgress();
            cardService.setTrackProgress(deckId, cardId, progress);
        }

        ApiResponse response = new ApiResponse("Card updated successfully",
                HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
