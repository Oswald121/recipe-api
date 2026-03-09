package com.recipeapp.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record RecipeDetailsResponse(
        Long id,
        String title,
        String shortDescription,
        String authorName,
        List<String> ingredients,
        List<String> steps,
        long likeCount,
        long dislikeCount,
        long score,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
