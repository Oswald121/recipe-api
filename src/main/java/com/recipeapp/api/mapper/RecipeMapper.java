package com.recipeapp.api.mapper;

import com.recipeapp.api.dto.RecipeDetailsResponse;
import com.recipeapp.api.dto.RecipeSummaryResponse;
import com.recipeapp.api.entity.Recipe;
import com.recipeapp.api.entity.RecipeIngredient;
import com.recipeapp.api.entity.RecipeStep;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeMapper {

    public RecipeSummaryResponse toSummary(Recipe recipe, long likeCount, long dislikeCount) {
        return new RecipeSummaryResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getAuthor().getDisplayName(),
                likeCount,
                dislikeCount,
                likeCount - dislikeCount,
                recipe.getCreatedAt()
        );
    }

    public RecipeDetailsResponse toDetails(Recipe recipe, long likeCount, long dislikeCount) {
        List<String> ingredients = recipe.getIngredients()
                .stream()
                .map(RecipeIngredient::getIngredientText)
                .toList();

        List<String> steps = recipe.getSteps()
                .stream()
                .map(RecipeStep::getStepText)
                .toList();

        return new RecipeDetailsResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getAuthor().getDisplayName(),
                ingredients,
                steps,
                likeCount,
                dislikeCount,
                likeCount - dislikeCount,
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }
}
