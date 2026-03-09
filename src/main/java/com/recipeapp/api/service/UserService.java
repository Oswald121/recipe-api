package com.recipeapp.api.service;

import com.recipeapp.api.dto.UserCreateRequest;
import com.recipeapp.api.dto.UserResponse;
import com.recipeapp.api.entity.AppUser;
import com.recipeapp.api.exception.BadRequestException;
import com.recipeapp.api.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public UserResponse createUser(UserCreateRequest request) {
        if (request.email() != null && !request.email().isBlank()
                && appUserRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new BadRequestException("A user with this email already exists.");
        }

        AppUser user = new AppUser(request.displayName().trim(), normalizeEmail(request.email()));
        AppUser saved = appUserRepository.save(user);

        return new UserResponse(saved.getId(), saved.getDisplayName(), saved.getEmail(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getDisplayName(), user.getEmail(), user.getCreatedAt()))
                .toList();
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}
