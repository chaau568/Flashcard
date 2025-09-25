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

import com.chaau568.flashcards.datatype.AddCardToDeckForm;
import com.chaau568.flashcards.datatype.CardCreateForm;
import com.chaau568.flashcards.datatype.CardProgressUpdate;
import com.chaau568.flashcards.datatype.CardUpdateForm;
import com.chaau568.flashcards.datatype.DeckInfo;
import com.chaau568.flashcards.datatype.DeckUpdateForm;
import com.chaau568.flashcards.datatype.UpdateCardForm;
import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.Deck;
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

    @GetMapping("/user/get_by_name/{username}")
    public ResponseEntity<ApiResponseWithData> getUserByName(@PathVariable String username) {
        User user = userService.loadUserByUsername(username);
        ApiResponseWithData response = new ApiResponseWithData("User getted by username successfully.",
                HttpStatus.OK.value(), user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Deck
    @GetMapping("/deck/get_info/{deckId}")
    public ResponseEntity<ApiResponseWithData> getDeckInfo(HttpServletRequest request, @PathVariable String deckId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        DeckInfo deckInfo = deckService.getDeckInfo(deckId);

        ApiResponseWithData response = new ApiResponseWithData("Deck created successfully.",
                HttpStatus.OK.value(), deckInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deck/create")
    public ResponseEntity<ApiResponseWithData> createDeck(@RequestBody Deck newDeck, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String userId = (String) session.getAttribute("userId");
        String deckId = userService.addOwnerDeck(userId, newDeck); // สร้าง deckId ใน ownerUser ก่อน แล้วค่อยสร้าง deck
                                                                   // ที่หลัง

        ApiResponseWithData response = new ApiResponseWithData("Deck created successfully.",
                HttpStatus.CREATED.value(), deckId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/deck/create_card/")
    public ResponseEntity<ApiResponse> createCardTODeckByDeckId(HttpServletRequest request,
            @RequestBody AddCardToDeckForm newCard) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String ownerUserId = (String) session.getAttribute("userId");
        String ownerDeckId = newCard.getDeckId();

        for (Card card : newCard.getResults()) {
            deckService.addCard(ownerUserId, ownerDeckId, card);
        }

        ApiResponse response = new ApiResponse("Add card to deck successfully.", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/deck/update")
    public ResponseEntity<ApiResponse> updateDeckByOwnerDeck(HttpServletRequest request,
            @RequestBody DeckUpdateForm deck) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String ownerUserId = (String) session.getAttribute("userId");
        String ownerDeckId = deck.getDeckId();
        Deck updateDeck = deck.getDeck();

        deckService.updateDeck(ownerUserId, ownerDeckId, updateDeck);

        ApiResponse response = new ApiResponse("Update deck successfully.", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deck/delete/{deckId}")
    public ResponseEntity<ApiResponse> deleteDeckByOwnerDeckId(HttpServletRequest request,
            @PathVariable String deckId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String ownerUserId = (String) session.getAttribute("userId");
        deckService.deleteDeck(ownerUserId, deckId);

        ApiResponse response = new ApiResponse("Delete deck successfully.", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

    // Card
    @GetMapping("/card/get_by_deck_id/{ownerId}")
    public ResponseEntity<ApiResponseWithData> getAllCardsByDeckId(HttpServletRequest request,
            @PathVariable String ownerId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }
        List<Card> deckList = cardService.loadAllCardsFromOwnerDeckIdThatExisting(ownerId);
        ApiResponseWithData response = new ApiResponseWithData("All Cards getted by owner deck id successfully.",
                HttpStatus.OK.value(), deckList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/card/update")
    public ResponseEntity<ApiResponse> updateCard(HttpServletRequest request, @RequestBody CardUpdateForm updateCard) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new SessionNotFound("Session not found or Session expired.");
        }

        String ownerDeckId = updateCard.getOwnerDeckId();
        String cardId = updateCard.getCardId();
        Card card = updateCard.getCard();

        cardService.updateCard(ownerDeckId, cardId, card);

        ApiResponse response = new ApiResponse("Card update successfully",
                HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/card/update_progress_card")
    public ResponseEntity<ApiResponseWithData> updateProgressCard(HttpServletRequest request,
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

        List<Card> deckList = cardService.loadAllCardsFromOwnerDeckId(deckId);
        ApiResponseWithData response = new ApiResponseWithData("Card updated successfully", HttpStatus.OK.value(),
                deckList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
