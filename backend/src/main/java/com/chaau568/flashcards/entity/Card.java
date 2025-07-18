package com.chaau568.flashcards.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Card {
    @Id
    @Getter
    @Setter(AccessLevel.NONE)
    private String id;

    @Getter
    @Setter(AccessLevel.NONE)
    private String ownerDeckId;

    // private String imageUrl;
    private String frontCard;
    private String backCard;
    private String state;
    private Integer progress;
    private LocalDateTime processingTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void assignToDeck(String deckId) {
        this.ownerDeckId = deckId;
    }

}
