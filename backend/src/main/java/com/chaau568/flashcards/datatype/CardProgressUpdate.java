package com.chaau568.flashcards.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CardProgressUpdate {
    private String cardId;
    private String progress;
}
