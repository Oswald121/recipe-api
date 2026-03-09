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
@Table(name = "recipe_steps")
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "step_text", nullable = false, columnDefinition = "TEXT")
    private String stepText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public RecipeStep() {
    }

    public RecipeStep(Integer sortOrder, String stepText) {
        this.sortOrder = sortOrder;
        this.stepText = stepText;
    }

    public Long getId() {
        return id;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getStepText() {
        return stepText;
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

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
