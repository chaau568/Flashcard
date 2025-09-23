package com.chaau568.flashcards.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdateCardForm {
    private String deckId;
    private List<CardProgressUpdate> results;
}
