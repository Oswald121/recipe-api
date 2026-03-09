package com.recipeapp.api.dto;

import java.time.OffsetDateTime;

public record RecipeSummaryResponse(
        Long id,
        String title,
        String shortDescription,
        String authorName,
        long likeCount,
        long dislikeCount,
        long score,
        OffsetDateTime createdAt
) {
}
