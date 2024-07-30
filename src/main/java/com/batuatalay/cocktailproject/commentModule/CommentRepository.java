package com.batuatalay.cocktailproject.commentModule;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Object> findByRecipe(String recipe);
}
