package com.recipeapp.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name can be at most 100 characters")
        String displayName,

        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email can be at most 150 characters")
        String email
) {
}
