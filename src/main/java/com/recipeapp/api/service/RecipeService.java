package com.recipeapp.api.service;

import com.recipeapp.api.dto.ReactionRequest;
import com.recipeapp.api.dto.RecipeCreateRequest;
import com.recipeapp.api.dto.RecipeDetailsResponse;
import com.recipeapp.api.dto.RecipeSummaryResponse;
import com.recipeapp.api.entity.AppUser;
import com.recipeapp.api.entity.Recipe;
import com.recipeapp.api.entity.RecipeIngredient;
import com.recipeapp.api.entity.RecipeReaction;
import com.recipeapp.api.entity.RecipeStep;
import com.recipeapp.api.exception.BadRequestException;
import com.recipeapp.api.exception.NotFoundException;
import com.recipeapp.api.mapper.RecipeMapper;
import com.recipeapp.api.repository.AppUserRepository;
import com.recipeapp.api.repository.RecipeReactionRepository;
import com.recipeapp.api.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecipeService {

    private static final String SORT_LATEST = "latest";
    private static final String SORT_MOST_LIKED = "mostLiked";

    private final RecipeRepository recipeRepository;
    private final AppUserRepository appUserRepository;
    private final RecipeReactionRepository recipeReactionRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(
            RecipeRepository recipeRepository,
            AppUserRepository appUserRepository,
            RecipeReactionRepository recipeReactionRepository,
            RecipeMapper recipeMapper
    ) {
        this.recipeRepository = recipeRepository;
        this.appUserRepository = appUserRepository;
        this.recipeReactionRepository = recipeReactionRepository;
        this.recipeMapper = recipeMapper;
    }

    public RecipeDetailsResponse createRecipe(RecipeCreateRequest request) {
        AppUser author = appUserRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundException("Author not found with id " + request.authorId()));

        Recipe recipe = new Recipe(author, request.title().trim(), request.shortDescription().trim());

        for (int i = 0; i < request.ingredients().size(); i++) {
            String ingredientText = request.ingredients().get(i).trim();
            recipe.addIngredient(new RecipeIngredient(i + 1, ingredientText));
        }

        for (int i = 0; i < request.steps().size(); i++) {
            String stepText = request.steps().get(i).trim();
            recipe.addStep(new RecipeStep(i + 1, stepText));
        }

        Recipe saved = recipeRepository.save(recipe);
        return recipeMapper.toDetails(saved, 0, 0);
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryResponse> getRecipes(String search, String sort) {
        String normalizedSort = normalizeSort(sort);

        List<Recipe> recipes = hasText(search)
                ? recipeRepository.search(search.trim())
                : recipeRepository.findAll();

        Map<Long, CountSummary> counts = summarizeCountsForRecipes(recipes.stream().map(Recipe::getId).toList());

        Comparator<RecipeSummaryResponse> comparator = SORT_MOST_LIKED.equals(normalizedSort)
                ? Comparator.comparingLong(RecipeSummaryResponse::likeCount)
                    .thenComparingLong(RecipeSummaryResponse::score)
                    .thenComparing(RecipeSummaryResponse::createdAt)
                    .reversed()
                : Comparator.comparing(RecipeSummaryResponse::createdAt).reversed();

        return recipes.stream()
                .map(recipe -> {
                    CountSummary summary = counts.getOrDefault(recipe.getId(), CountSummary.zero());
                    return recipeMapper.toSummary(recipe, summary.likeCount(), summary.dislikeCount());
                })
                .sorted(comparator)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecipeDetailsResponse getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found with id " + recipeId));

        recipe.getIngredients().size();
        recipe.getSteps().size();

        Map<Long, CountSummary> counts = summarizeCountsForRecipes(List.of(recipeId));
        CountSummary summary = counts.getOrDefault(recipeId, CountSummary.zero());

        return recipeMapper.toDetails(recipe, summary.likeCount(), summary.dislikeCount());
    }

    public RecipeDetailsResponse reactToRecipe(Long recipeId, ReactionRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found with id " + recipeId));

        AppUser user = appUserRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User not found with id " + request.userId()));

        RecipeReaction reaction = recipeReactionRepository.findByRecipeIdAndUserId(recipeId, request.userId())
                .orElseGet(() -> new RecipeReaction(request.reactionType(), recipe, user));

        reaction.setType(request.reactionType());
        reaction.setRecipe(recipe);
        reaction.setUser(user);
        recipeReactionRepository.save(reaction);

        return getRecipeById(recipeId);
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryResponse> getTopLikedRecipes(int limit) {
        if (limit < 1 || limit > 100) {
            throw new BadRequestException("Limit must be between 1 and 100.");
        }

        return getRecipes(null, SORT_MOST_LIKED)
                .stream()
                .limit(limit)
                .toList();
    }

    private Map<Long, CountSummary> summarizeCountsForRecipes(Collection<Long> recipeIds) {
        Map<Long, CountSummary> counts = new HashMap<>();
        if (recipeIds == null || recipeIds.isEmpty()) {
            return counts;
        }

        recipeReactionRepository.summarizeByRecipeIds(recipeIds).forEach(row -> {
            long likeCount = row.getLikeCount() == null ? 0 : row.getLikeCount();
            long dislikeCount = row.getDislikeCount() == null ? 0 : row.getDislikeCount();
            counts.put(row.getRecipeId(), new CountSummary(likeCount, dislikeCount));
        });

        return counts;
    }

    private String normalizeSort(String sort) {
        if (!hasText(sort)) {
            return SORT_LATEST;
        }

        String normalized = sort.trim();
        if (!SORT_LATEST.equals(normalized) && !SORT_MOST_LIKED.equals(normalized)) {
            throw new BadRequestException("Sort must be either 'latest' or 'mostLiked'.");
        }
        return normalized;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record CountSummary(long likeCount, long dislikeCount) {
        static CountSummary zero() {
            return new CountSummary(0, 0);
        }
    }
}
