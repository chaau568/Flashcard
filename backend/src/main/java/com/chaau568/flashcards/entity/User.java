package com.chaau568.flashcards.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users") // ชื่อตรงกับ collection ใน database ที่เราสร้างไว้
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// ระบบจะ auto create method getter setter ให้ แล้ว id จะมีเเค่ getter ที่เป็น
// public เท่านั้น setter เป็น private

public class User {
    @Id
    @Getter
    @Setter(AccessLevel.NONE) // set ได้แค่ owner
    private String id;

    private String username;
    private String password;
    private List<String> ownerDeckIds;
    private List<String> savedPublicDeckIds;

}
