package com.batuatalay.cocktailproject.recipeModule;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Recipe findByCode(String code);
}
