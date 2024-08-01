package com.batuatalay.cocktailproject.recipeModule;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Recipe findByCode(String code);
    List<Recipe> findAllByType(String type);
}
