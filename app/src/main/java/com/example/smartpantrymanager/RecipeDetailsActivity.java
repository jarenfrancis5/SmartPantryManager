package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView recipeNameTextView;
    private TextView ingredientsTextView;
    private TextView preparationStepsTextView;

    private DatabaseHelper databaseHelper;

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Connect Java variables to XML views
        MaterialToolbar toolbar =
                findViewById(R.id.recipe_details_toolbar);

        recipeNameTextView =
                findViewById(R.id.recipe_details_name);

        ingredientsTextView =
                findViewById(R.id.recipe_details_ingredients);

        preparationStepsTextView =
                findViewById(R.id.recipe_details_steps);


        // Back arrow
        toolbar.setNavigationOnClickListener(v -> finish());


        // Create database helper
        databaseHelper = new DatabaseHelper(this);


        // Get the recipe ID passed from ViewRecipesActivity
        recipeId = getIntent().getIntExtra(
                "recipe_id",
                -1
        );


        // Check that a valid recipe ID was received
        if (recipeId != -1) {

            loadRecipeDetails();

        } else {

            recipeNameTextView.setText("Recipe not found");
        }
    }


    private void loadRecipeDetails() {

        // Get selected recipe
        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {

            recipeNameTextView.setText("Recipe not found");
            return;
        }


        // Display recipe name
        recipeNameTextView.setText(
                recipe.getRecipeName()
        );


        // Display preparation steps
        preparationStepsTextView.setText(
                recipe.getPreparationSteps()
        );


        // Get ingredients belonging to this recipe
        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);


        // Build readable ingredient list
        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient.getIngredientName())
                    .append(" - ")
                    .append(ingredient.getRequiredQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }


        // Display ingredients
        if (ingredients.isEmpty()) {

            ingredientsTextView.setText(
                    "No ingredients found."
            );

        } else {

            ingredientsTextView.setText(
                    ingredientText.toString()
            );
        }
    }
}