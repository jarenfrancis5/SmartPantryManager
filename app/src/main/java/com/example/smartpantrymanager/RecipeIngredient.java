package com.example.smartpantrymanager;

public class RecipeIngredient {

    private int ingredientId;
    private int recipeId;
    private String ingredientName;
    private double requiredQuantity;
    private String unit;

    // Constructor
    public RecipeIngredient(
            int ingredientId,
            int recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    // Getters
    public int getIngredientId() {
        return ingredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public String getUnit() {
        return unit;
    }

    // Setters
    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
