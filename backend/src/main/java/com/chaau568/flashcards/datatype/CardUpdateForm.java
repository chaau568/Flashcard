package com.chaau568.flashcards.datatype;

import com.chaau568.flashcards.entity.Card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CardUpdateForm {
    private String ownerDeckId;
    private String cardId;
    private Card card;
}
