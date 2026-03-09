package com.recipeapp.api.repository;

import com.recipeapp.api.entity.RecipeReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecipeReactionRepository extends JpaRepository<RecipeReaction, Long> {

    Optional<RecipeReaction> findByRecipeIdAndUserId(Long recipeId, Long userId);

    @Query("""
            select rr.recipe.id as recipeId,
                   sum(case when rr.type = com.recipeapp.api.entity.ReactionType.LIKE then 1 else 0 end) as likeCount,
                   sum(case when rr.type = com.recipeapp.api.entity.ReactionType.DISLIKE then 1 else 0 end) as dislikeCount
            from RecipeReaction rr
            where rr.recipe.id in :recipeIds
            group by rr.recipe.id
            """)
    List<RecipeReactionCountProjection> summarizeByRecipeIds(@Param("recipeIds") Collection<Long> recipeIds);

    interface RecipeReactionCountProjection {
        Long getRecipeId();
        Long getLikeCount();
        Long getDislikeCount();
    }
}
