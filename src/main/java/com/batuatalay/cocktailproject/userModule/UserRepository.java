package com.batuatalay.cocktailproject.userModule;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository <User, String> {
    User findByauthToken(String authToken);
    List<User> findByStatus(String status);
    User findByUsername(String username);
}
