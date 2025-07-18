package com.chaau568.flashcards.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "decks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Deck {
    @Id
    @Getter
    @Setter(AccessLevel.NONE)
    private String id;

    @Getter
    @Setter(AccessLevel.NONE)
    private String ownerUserId;

    private Boolean isPublic;
    private List<String> cardListId;
    private List<String> tagList;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void assignToOwnerUserId(String userId) {
        this.ownerUserId = userId;
    }
}
