package com.chaau568.flashcards.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HybridPayload {
    private String encryptedKey; // AES key ที่เข้ารหัสด้วย RSA
    private String encryptedData; // ข้อมูลที่เข้ารหัสด้วย AES
}
