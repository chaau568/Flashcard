package com.chaau568.flashcards.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chaau568.flashcards.entity.User;

@Repository

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
