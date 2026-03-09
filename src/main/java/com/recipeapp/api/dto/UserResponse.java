package com.recipeapp.api.dto;

import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        String displayName,
        String email,
        OffsetDateTime createdAt
) {
}
