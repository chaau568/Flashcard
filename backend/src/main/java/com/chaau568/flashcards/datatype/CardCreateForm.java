package com.chaau568.flashcards.datatype;

import java.util.List;

import com.chaau568.flashcards.entity.Card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CardCreateForm {
    private String ownerDeckId;
    private List<Card> card;
}
