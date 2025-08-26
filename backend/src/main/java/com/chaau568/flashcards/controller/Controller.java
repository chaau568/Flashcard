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
import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.response.ApiResponse;
import com.chaau568.flashcards.response.ApiResponseWithData;
import com.chaau568.flashcards.service.CardService;
import com.chaau568.flashcards.service.DeckService;
import com.chaau568.flashcards.service.UserService;

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

    // Greeting
    @GetMapping
    public ResponseEntity<ApiResponse> hello() {
        ApiResponse response = new ApiResponse("Hello, you can connect spring boot!", HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody User newUser) {
        userService.createAccount(newUser);
        System.out.println(newUser);
        ApiResponse response = new ApiResponse("User created successfully.", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Map<String, String> user, HttpSession session) {
        String username = user.get("username");
        String password = user.get("password");
        userService.login(username, password);
        session.setAttribute("username", username);
        ApiResponse response = new ApiResponse("User logined successfully.", HttpStatus.OK.value());
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

    // @GetMapping("/user/get_by_name/{username}")
    // public ResponseEntity<ApiResponseWithData> getUserByName(@PathVariable String
    // username) {
    // User user = userService.getUserByUsername(username);
    // ApiResponseWithData response = new ApiResponseWithData("User getted by
    // username successfully",
    // HttpStatus.OK.value(), user);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

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
    // @PostMapping("/deck/create")
    // public ResponseEntity<ApiResponse> createDeck(@RequestBody Deck newDeck) {
    // deckService.createDeck(newDeck);
    // ApiResponse response = new ApiResponse("Deck created successfully",
    // HttpStatus.CREATED.value());
    // return new ResponseEntity<>(response, HttpStatus.CREATED);
    // }

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

    // @GetMapping("/deck/get_by_user/{userId}")
    // public ResponseEntity<ApiResponseWithData> getDeckByUserId(@PathVariable
    // String userId) {
    // List<Deck> deckList = deckService.getAllByOwnerUserId(userId);
    // ApiResponseWithData response = new ApiResponseWithData("Deck getted by user
    // id successfully",
    // HttpStatus.OK.value(),
    // deckList);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

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
    // @PostMapping("/card/create")
    // public ResponseEntity<ApiResponse> createCard(@RequestBody Card newCard) {
    // cardService.createCard(newCard);
    // ApiResponse response = new ApiResponse("Card created successfully",
    // HttpStatus.CREATED.value());
    // return new ResponseEntity<>(response, HttpStatus.CREATED);
    // }
}
