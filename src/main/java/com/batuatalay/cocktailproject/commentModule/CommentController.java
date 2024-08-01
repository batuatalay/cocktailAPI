package com.batuatalay.cocktailproject.commentModule;



import com.batuatalay.cocktailproject.helperModule.*;
import com.batuatalay.cocktailproject.recipeModule.*;
import com.batuatalay.cocktailproject.userModule.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private HelperController helperController;
    @Autowired
    private UserController userController;
    @Autowired
    private RecipeController recipeController;
    @Autowired
    private RecipeRepository recipeRepository;


    @GetMapping()
    public Object getAllComments(@RequestHeader("Authorization") String authorizationHeader) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            returnData = helperController.prepareReturn("200",commentRepository.findAll());
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @GetMapping("/recipe/{recipe}")
    public Object getAllCommentsByRecipeID(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("recipe") String recipe)  {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            Recipe currentRecipe = recipeRepository.findByCode(recipe);
            List<Object> comments;
            if (currentRecipe == null) {
                comments = commentRepository.findByRecipe(recipe);
            } else {
                comments = commentRepository.findByRecipe(currentRecipe.getId());
            }
            if (comments.isEmpty()) {
                returnData = helperController.prepareReturn("200", "There is no comment for this recipe");
            } else {
                returnData = helperController.prepareReturn("200", comments);
            }
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @PostMapping()
    public Object addComment(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Comment comment) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            String recipeCodeOrId = comment.getRecipe();
            Recipe currentRecipe = recipeRepository.findByCode(recipeCodeOrId);

            if (currentRecipe == null) {
                try {
                    currentRecipe = recipeRepository.findById(recipeCodeOrId).orElse(null);
                } catch (Exception e) {
                    return helperController.prepareReturn("400", "Invalid recipe ID");
                }
                if (currentRecipe == null) {
                    return helperController.prepareReturn("401", "There is no recipe");
                }
            }

            comment.setRecipe(currentRecipe.getId());
            commentRepository.save(comment);
            returnData = helperController.prepareReturn("200", "Comment successfully added");
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }

    @DeleteMapping("/{commentID}")
    public Object deleteComment(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("commentID") String commentID) {
        Object returnData;
        if (userController.loginCheck(authorizationHeader)) {
            try {
                Comment comment = commentRepository.findById(commentID).orElse(null);
                commentRepository.deleteById(comment.getId());
            } catch (Exception e) {
                return helperController.prepareReturn("400", "Invalid comment ID");
            }
            returnData = helperController.prepareReturn("200", "Comment successfully deleted");
        } else {
            returnData = helperController.prepareReturn("401", "Unauthorized");
        }
        return returnData;
    }


}
