package com.batuatalay.cocktailproject.typeModule;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TypeRepository extends MongoRepository<Type, String> {
    Type findByCode(String code);
    List<Object> findByStatus(String status);
}
