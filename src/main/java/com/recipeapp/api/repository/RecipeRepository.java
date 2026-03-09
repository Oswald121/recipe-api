package com.recipeapp.api.repository;

import com.recipeapp.api.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @EntityGraph(attributePaths = {"author"})
    @Query("""
            select r
            from Recipe r
            join r.author a
            where lower(r.title) like lower(concat('%', :search, '%'))
               or lower(r.shortDescription) like lower(concat('%', :search, '%'))
               or lower(a.displayName) like lower(concat('%', :search, '%'))
            order by r.createdAt desc
            """)
    List<Recipe> search(@Param("search") String search);

    @Override
    @EntityGraph(attributePaths = {"author"})
    List<Recipe> findAll();

    @EntityGraph(attributePaths = {"author"})
    Optional<Recipe> findById(Long id);
}
