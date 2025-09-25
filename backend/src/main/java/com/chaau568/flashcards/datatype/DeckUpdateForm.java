package com.chaau568.flashcards.datatype;

import com.chaau568.flashcards.entity.Deck;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeckUpdateForm {
    private String deckId;
    private Deck deck;
}
