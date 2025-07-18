package com.chaau568.flashcards.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chaau568.flashcards.entity.Card;

@Repository

public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> findAllByOwnerDeckId(String ownerDeckId);
}
