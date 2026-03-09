package com.recipeapp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "ingredient_text", nullable = false, length = 255)
    private String ingredientText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public RecipeIngredient() {
    }

    public RecipeIngredient(Integer sortOrder, String ingredientText) {
        this.sortOrder = sortOrder;
        this.ingredientText = ingredientText;
    }

    public Long getId() {
        return id;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getIngredientText() {
        return ingredientText;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setIngredientText(String ingredientText) {
        this.ingredientText = ingredientText;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
