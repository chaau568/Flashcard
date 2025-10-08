package com.chaau568.flashcards.util;

import com.chaau568.flashcards.datatype.AddCardToDeckForm;
import com.chaau568.flashcards.datatype.CardUpdateForm;
import com.chaau568.flashcards.datatype.DeckInfo;
import com.chaau568.flashcards.datatype.DeckUpdateForm;
import com.chaau568.flashcards.datatype.UpdateCardForm;
import com.chaau568.flashcards.entity.Card;
import com.chaau568.flashcards.entity.Deck;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.response.ApiResponseWithData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.List;
import java.util.Map;

public class ApiHelper {
    private final String BASE_URL;
    private final HttpClient client;
    private final ObjectMapper mapper;

    private String jSessionId;

    public ApiHelper(String baseUrl) {
        this.BASE_URL = baseUrl;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.mapper = new ObjectMapper();
    }

    // User
    public boolean authen() throws Exception {
        String url = BASE_URL + "/authen";

        if (jSessionId == null || jSessionId.isEmpty()) {
            return false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", this.jSessionId)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else if (response.statusCode() == 401) {
            return false;
        } else {
            throw new RuntimeException("Authen check failed with status: " + response.statusCode());
        }
    }

    public String login(String username, String password) throws Exception {
        String url = BASE_URL + "/user/login";
        Map<String, String> payload = Map.of("username", username, "password", password);
        String body = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Login failed: " + response.body());
        }

        response.headers().firstValue("Set-Cookie").ifPresent(cookieHeader -> {
            String[] parts = cookieHeader.split(";")[0].split("=");
            if (parts.length > 1 && parts[0].equals("JSESSIONID")) {
                jSessionId = parts[0] + "=" + parts[1];
            }
        });

        return jSessionId;
    }

    public void logout() throws Exception {
        String url = BASE_URL + "/user/logout";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId)
                .POST(BodyPublishers.noBody()).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Logout failed: " + response.body());
        }

        this.jSessionId = null;
    }

    public String register(User user) throws Exception {
        String url = BASE_URL + "/user/register";

        String body = mapper.writeValueAsString(user);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("Register failed: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(), String.class);
    }

    public void deleteAccount(String userId) throws Exception {
        String url = BASE_URL + "/user/delete/" + userId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).DELETE()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Delect account failed: " + response.body());
        }
    }

    public String getUserIdFromSession() throws Exception {
        String url = BASE_URL + "/user/apihelper/get_user_id/";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get userId from session.");
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(), String.class);
    }

    // Deck
    public DeckInfo getDeckInfo(String deckId) throws Exception {
        String url = BASE_URL + "/deck/get_info/" + deckId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load deck info of ID: " + deckId + ": " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(), DeckInfo.class);
    }

    public List<Deck> getHomeDecks() throws Exception {
        String url = BASE_URL + "/deck/get_by_public";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load public decks: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(
                apiResp.getData(),
                mapper.getTypeFactory().constructCollectionType(List.class, Deck.class));
    }

    public List<Deck> getMyDecks() throws Exception {
        String url = BASE_URL + "/deck/get_by_owner_user_id";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load user's decks: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(
                apiResp.getData(),
                mapper.getTypeFactory().constructCollectionType(List.class, Deck.class));
    }

    public void createCardToDeckByDeckId(AddCardToDeckForm addCardToDeckForm) throws Exception {
        String url = BASE_URL + "/deck/create_card/";

        String body = mapper.writeValueAsString(addCardToDeckForm);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .header("Cookie",
                        this.jSessionId)
                .POST(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("Create cards to deck failed: " + response.body());
        }
    }

    public String createDeck(Deck deck) throws Exception {
        String url = BASE_URL + "/deck/create";

        String body = mapper.writeValueAsString(deck);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .header("Cookie", this.jSessionId).POST(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("Create deck failed: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(), String.class);
    }

    public void updateDeck(DeckUpdateForm deck) throws Exception {
        String url = BASE_URL + "/deck/update";

        String body = mapper.writeValueAsString(deck);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .header("Cookie", this.jSessionId).PUT(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Update deck failed: " + response.body());
        }
    }

    public String getLastCreatedDeckId() throws Exception {
        String url = BASE_URL + "/deck/get_by_owner_user_id";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie", this.jSessionId).GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Get last create deck failed: " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> respMap = mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {
        });
        List<Map<String, Object>> deckList = (List<Map<String, Object>>) respMap.get("data");

        if (deckList == null || deckList.isEmpty()) {
            return null;
        }

        return deckList.get(deckList.size() - 1).get("id").toString();
    }

    public void deleteDeck(String deckId) throws Exception {
        String url = BASE_URL + "/deck/delete/" + deckId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).DELETE()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Delete deck failed: " + response.body());
        }
    }

    // Card
    public List<Card> getAllCardsByDeckId(String deckId) throws Exception {
        String url = BASE_URL + "/card/get_by_deck_id/" + deckId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie",
                this.jSessionId).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load deck's cards: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(),
                mapper.getTypeFactory().constructCollectionType(List.class, Card.class));
    }

    public void updateCard(CardUpdateForm card) throws Exception {
        String url = BASE_URL + "/card/update";

        String body = mapper.writeValueAsString(card);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .header("Cookie", this.jSessionId).PUT(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Update progress card failed: " + response.body());
        }
    }

    public List<Card> updateProgressCard(UpdateCardForm card) throws Exception {
        String url = BASE_URL + "/card/update_progress_card";

        String body = mapper.writeValueAsString(card);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .header("Cookie", this.jSessionId).PUT(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Update progress card failed: " + response.body());
        }

        ApiResponseWithData apiResp = mapper.readValue(response.body(), ApiResponseWithData.class);

        return mapper.convertValue(apiResp.getData(),
                mapper.getTypeFactory().constructCollectionType(List.class, Card.class));
    }
}
