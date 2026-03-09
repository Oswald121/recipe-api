package com.recipeapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RecipeCreateRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title can be at most 150 characters")
        String title,

        @NotBlank(message = "Short description is required")
        @Size(max = 255, message = "Short description can be at most 255 characters")
        String shortDescription,

        @NotNull(message = "Author id is required")
        Long authorId,

        @NotEmpty(message = "At least one ingredient is required")
        List<@NotBlank(message = "Ingredient cannot be blank") @Size(max = 255, message = "Ingredient can be at most 255 characters") String> ingredients,

        @NotEmpty(message = "At least one step is required")
        List<@NotBlank(message = "Step cannot be blank") String> steps
) {
}
