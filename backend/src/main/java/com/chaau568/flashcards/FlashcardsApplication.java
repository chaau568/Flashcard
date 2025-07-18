package com.chaau568.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing // @EnableMongoAuditing คือ Annotation ที่ใช้เปิดการทำงานของ MongoDB Auditing ใน Spring Boot ใช้ create time auto
public class FlashcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardsApplication.class, args);
	}

}
