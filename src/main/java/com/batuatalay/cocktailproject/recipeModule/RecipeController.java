package com.batuatalay.cocktailproject.recipeModule;

import com.batuatalay.cocktailproject.helperModule.HelperController;
import com.batuatalay.cocktailproject.userModule.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserController userController;
    @Autowired
    private HelperController helperController;

    @GetMapping("/all")
    public Object getAll(@RequestHeader("Authorization") String authorizationHeader) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            for (Recipe recipe : recipeRepository.findAll()) {
                recipe.setType("66ab38adca817008958b167a");
                recipeRepository.save(recipe);

            }
            returnData = helperController.prepareReturn("200",recipeRepository.findAll());
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/{id}")
    public Object getOne(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            returnData = helperController.prepareReturn("200",recipeRepository.findById(id).orElse(null));
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/name")
    public Object getAllCocktailsName(@RequestHeader("Authorization") String authorizationHeader) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            ArrayList<Map<String, String>> names = new ArrayList<>();
            List<Recipe> recipes = recipeRepository.findAll();
            for (Recipe recipe : recipes) {
                Map<String, String> map = new HashMap<>();

                map.put("code", recipe.getCode());
                map.put("name", recipe.getName());
                names.add(map);
            }
            returnData = helperController.prepareReturn("200", names);
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/code/{code}")
    public Object getRecipeByCode(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("code") String code) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            returnData = helperController.prepareReturn("200", recipeRepository.findByCode(code));
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/search/{material}")
    public Object getRecipeByMaterial(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("material") String material) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            List<Recipe> recipes = recipeRepository.findAll();
            List <Recipe> returnRecipes = new ArrayList<>();
            for (Recipe recipe : recipes) {
                boolean found =false;
                List<String> materials = recipe.getMaterials();
                for (String mat : materials) {
                    if(mat.contains(material)) {
                        returnRecipes.add(recipe);
                        found = true;
                        break;
                    }
                }
                if(found) {
                    continue;
                }
            }
            if(returnRecipes.size() > 0) {
                returnData = helperController.prepareReturn("200", returnRecipes);
            } else {
                returnData = helperController.prepareReturn("200", "A cocktail made with the ingredients you are looking for could not be found.");
            }
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/random")
    public Object getRandomRecipe(@RequestHeader("Authorization") String authorizationHeader) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            List<Recipe> recipes = recipeRepository.findAll();
            Random random = new Random();
            int index = random.nextInt(recipes.size());
            returnData = helperController.prepareReturn("200", recipes.get(index));
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @PostMapping
    public Object save(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Recipe recipe) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            if(recipeRepository.findByCode(recipe.getCode()) == null) {
                recipeRepository.save(recipe);
                returnData = helperController.prepareReturn("200", "Recipe Saved");
            } else {
                returnData = helperController.prepareReturn("400", "recipe already exists");
            }
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @PostMapping("/multiple")
    public Object saveMultiple(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<Recipe> recipes) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            int flag = 0;
            for (Recipe recipe : recipes) {
                if(recipeRepository.findByCode(recipe.getCode()) == null) {
                    recipeRepository.save(recipe);
                    flag = flag + 1;
                }
            }
            if(flag > 0) {
                returnData = helperController.prepareReturn("200", flag + " recipes are saved");
            } else {
                returnData = helperController.prepareReturn("400", "These are already exist");
            }
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("/{id}")
    public Object delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            recipeRepository.deleteById(id);
            returnData = helperController.prepareReturn("200", "Recipe Deleted");
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("/code/{code}")
    public Object deleteByCode(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("code") String code) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            Recipe recipe = recipeRepository.findByCode(code);
            recipeRepository.delete(recipe);
            returnData = helperController.prepareReturn("200", "Recipe Deleted");
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("/many")
    public Object deleteMany(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<String> ids) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            for (String id : ids) {
                recipeRepository.deleteById(id);
            }
            returnData = helperController.prepareReturn("200", ids.size() + "Recipes are Deleted");
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("/many/code")
    public Object deleteManyByCode(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<String> codes) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            for (String code : codes) {
                Recipe recipe = recipeRepository.findByCode(code);
                recipeRepository.deleteById(recipe.getId());
            }
            returnData = helperController.prepareReturn("200", codes.size() + "Recipes are Deleted");
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @PutMapping("/{id}")
    public Object update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id, @RequestBody Recipe recipe) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            Recipe currentRecipe = recipeRepository.findById(id).orElse(null);
            if(currentRecipe != null) {
                currentRecipe.setName(recipe.getName());
                currentRecipe.setCode(recipe.getCode());
                currentRecipe.setDescription(recipe.getDescription());
                currentRecipe.setMaterials(recipe.getMaterials());
                recipeRepository.save(currentRecipe);
                returnData = helperController.prepareReturn("200", "Recipe Updated");
            } else {
                returnData = helperController.prepareReturn("401", "Recipe Not Found");
            }
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @PutMapping("code/{code}")
    public Object updateByCode(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String code, @RequestBody Recipe recipe) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            Recipe currentRecipe = recipeRepository.findByCode(code);
            if(currentRecipe != null) {
                currentRecipe.setName(recipe.getName());
                currentRecipe.setCode(recipe.getCode());
                currentRecipe.setDescription(recipe.getDescription());
                currentRecipe.setMaterials(recipe.getMaterials());
                recipeRepository.save(currentRecipe);
                returnData = helperController.prepareReturn("200", "Recipe Updated");
            } else {
                returnData = helperController.prepareReturn("401", "Recipe Not Found");
            }
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

    @PutMapping("/many")
    public Object updateMany(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<Recipe> recipes) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            for (Recipe recipe : recipes) {
                Recipe currentRecipe = recipeRepository.findById(recipe.getId()).orElse(null);
                currentRecipe.setName(recipe.getName());
                currentRecipe.setCode(recipe.getCode());
                currentRecipe.setDescription(recipe.getDescription());
                currentRecipe.setMaterials(recipe.getMaterials());
                recipeRepository.save(currentRecipe);
            }
            returnData = helperController.prepareReturn("200", recipes.size() + "Recipes are Updated");
        } else {
            returnData = helperController.prepareReturn("401","Unauthorized");
        }
        return returnData;
    }

}
