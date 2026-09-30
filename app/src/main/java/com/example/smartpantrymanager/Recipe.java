package com.example.smartpantrymanager;

public class Recipe {

    private int recipeId;
    private String recipeName;
    private String preparationSteps;

    // Constructor
    public Recipe(
            int recipeId,
            String recipeName,
            String preparationSteps) {

        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.preparationSteps = preparationSteps;
    }

    // Getters
    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getPreparationSteps() {
        return preparationSteps;
    }

    // Setters
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setPreparationSteps(String preparationSteps) {
        this.preparationSteps = preparationSteps;
    }
}