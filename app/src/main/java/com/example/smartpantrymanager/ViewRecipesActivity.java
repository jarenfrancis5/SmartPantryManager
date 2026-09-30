package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipesActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);

        // Connect Java variables to XML views
        MaterialToolbar toolbar =
                findViewById(R.id.recipe_toolbar);

        recipeRecyclerView =
                findViewById(R.id.recipe_recycler_view);

        // Back arrow
        toolbar.setNavigationOnClickListener(v -> finish());

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // Create recipe list
        recipeList = new ArrayList<>();

        // Configure RecyclerView
        recipeRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Create adapter
        recipeAdapter =
                new RecipeAdapter(recipeList);

        // Connect adapter to RecyclerView
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Load recipes from database
        loadRecipes();
    }

    private void loadRecipes() {

        recipeList.clear();

        recipeList.addAll(
                databaseHelper.getAllRecipes()
        );

        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (recipeAdapter != null) {
            loadRecipes();
        }
    }
}