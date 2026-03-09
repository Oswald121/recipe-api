package com.recipeapp.api.controller;

import com.recipeapp.api.dto.ReactionRequest;
import com.recipeapp.api.dto.RecipeCreateRequest;
import com.recipeapp.api.dto.RecipeDetailsResponse;
import com.recipeapp.api.dto.RecipeSummaryResponse;
import com.recipeapp.api.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDetailsResponse createRecipe(@Valid @RequestBody RecipeCreateRequest request) {
        return recipeService.createRecipe(request);
    }

    @GetMapping
    public List<RecipeSummaryResponse> getRecipes(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return recipeService.getRecipes(search, sort);
    }

    @GetMapping("/{recipeId}")
    public RecipeDetailsResponse getRecipeById(@PathVariable Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }

    @GetMapping("/top-liked")
    public List<RecipeSummaryResponse> getTopLikedRecipes(
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit
    ) {
        return recipeService.getTopLikedRecipes(limit);
    }

    @PostMapping("/{recipeId}/reactions")
    public RecipeDetailsResponse reactToRecipe(
            @PathVariable Long recipeId,
            @Valid @RequestBody ReactionRequest request
    ) {
        return recipeService.reactToRecipe(recipeId, request);
    }
}
