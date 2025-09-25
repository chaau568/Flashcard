package com.chaau568.flashcards.datatype;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeckInfo {
    private String ownerUsername;
    private Boolean isPublic;
    private String deckName;
    private List<String> tagList;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}