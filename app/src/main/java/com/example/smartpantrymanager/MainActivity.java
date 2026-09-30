package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Connect dashboard cards to Java
        MaterialCardView viewPantryCard =
                findViewById(R.id.view_pantry_card);

        MaterialCardView recipesCard =
                findViewById(R.id.recipes_card);


        // Open ViewPantryActivity
        viewPantryCard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ViewPantryActivity.class
            );

            startActivity(intent);
        });


        // Open ViewRecipesActivity
        recipesCard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ViewRecipesActivity.class
            );

            startActivity(intent);
        });
    }
}