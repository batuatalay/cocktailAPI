package com.batuatalay.cocktailproject.recipeModule;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    private String name;
    private String code;

    @Field("description")
    private List<String> description;
    @Field("materials")
    private List<String> materials;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }
}
