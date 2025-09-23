package com.chaau568.flashcards.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chaau568.flashcards.entity.Deck;

@Repository

public interface DeckRepository extends MongoRepository<Deck, String> {
    List<Deck> findAllByOwnerUserId(String id);

    List<Deck> findAllByIdIn(List<String> id);

    List<Deck> findAllByTagList(String tag);

    List<Deck> findAllByTagListIn(List<String> tags);

    List<Deck> findAllByIsPublicTrue();

    // List<Deck> findAllByCardListId(String cardListId);
}
