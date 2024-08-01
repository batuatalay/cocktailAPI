package com.batuatalay.cocktailproject.typeModule;

import com.batuatalay.cocktailproject.helperModule.HelperController;
import com.batuatalay.cocktailproject.recipeModule.*;
import com.batuatalay.cocktailproject.userModule.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/types")
public class TypeController {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UserController userController;
    @Autowired
    private HelperController helperController;
    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping
    public Object createType(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Type type) {
        Object returnData;
        if(userController.loginCheck(authorizationHeader)) {
            System.out.println(typeRepository.findByCode(type.getCode()));
            if(typeRepository.findByCode(type.getCode()) == null) {
                typeRepository.save(type);
                returnData = helperController.prepareReturn("200", "Type Successfully Created");
            } else {
                returnData = helperController.prepareReturn("400", "Type already exists");
            }
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @GetMapping
    public Object getAllTypes(@RequestHeader("Authorization") String authorizationHeader) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            returnData = helperController.prepareReturn("200", typeRepository.findAll());
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @GetMapping("{code}")
    public Object getTypeByCode(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String code) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            Type currentType = typeRepository.findByCode(code);
            if (currentType != null) {
                List<Recipe> recipes = recipeRepository.findAllByType(currentType.getId());
                returnData = helperController.prepareReturn("200", recipes);
            } else {
                returnData = helperController.prepareReturn("400", "This type does not exist");
            }
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }
    @PutMapping({"{id}"})
    public Object updateType(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String id, @RequestBody Type type) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            Type currentType = typeRepository.findById(id).orElse(null);
            if (currentType != null) {
                currentType.setName(type.getName());
                currentType.setCode(type.getCode());
                currentType.setDescription(type.getDescription());
                currentType.setStatus(type.getStatus());
                typeRepository.save(currentType);
                returnData = helperController.prepareReturn("200", "Type Successfully Updated");
            } else {
                returnData = helperController.prepareReturn("400", "This type does not exist");
            }
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("{id}")
    public Object deleteType(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            Type currentType = typeRepository.findById(id).orElse(null);
            if (currentType != null) {
                typeRepository.delete(currentType);
                returnData = helperController.prepareReturn("200", "Type Successfully Deleted");
            } else {
                returnData = helperController.prepareReturn("400", "This type does not exist");
            }
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }
}
