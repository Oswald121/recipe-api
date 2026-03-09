package com.recipeapp.api.dto;

import com.recipeapp.api.entity.ReactionType;
import jakarta.validation.constraints.NotNull;

public record ReactionRequest(
        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Reaction type is required")
        ReactionType reactionType
) {
}
