package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewPantryActivity extends AppCompatActivity
        implements PantryAdapter.OnPantryItemClickListener {

    // UI components
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyPantryTextView;
    private FloatingActionButton addPantryItemButton;

    // Database
    private DatabaseHelper databaseHelper;

    // RecyclerView
    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect this activity to activity_view_pantry.xml
        setContentView(R.layout.activity_view_pantry);

        // Find views from the XML layout
        toolbar = findViewById(R.id.view_pantry_toolbar);
        recyclerView = findViewById(R.id.recycler_view_pantry);
        emptyPantryTextView = findViewById(R.id.text_empty_pantry);
        addPantryItemButton = findViewById(R.id.button_add_pantry_item);

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up toolbar back button
        toolbar.setNavigationOnClickListener(view -> finish());

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load pantry items from database
        loadPantryItems();

        // Add pantry item button
        addPantryItemButton.setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            ViewPantryActivity.this,
                            AddPantryItemActivity.class
                    );

            startActivity(intent);
        });
    }


    /*
     * Gets all pantry items from the SQLite database
     * and displays them in the RecyclerView.
     */
    private void loadPantryItems() {

        pantryItems = databaseHelper.getAllPantryItems();

        pantryAdapter = new PantryAdapter(
                pantryItems,
                this
        );

        recyclerView.setAdapter(pantryAdapter);

        updateEmptyState();
    }


    /*
     * Shows "Your pantry is empty" when there
     * are no items in the database.
     */
    private void updateEmptyState() {

        if (pantryItems.isEmpty()) {

            recyclerView.setVisibility(View.GONE);
            emptyPantryTextView.setVisibility(View.VISIBLE);

        } else {

            recyclerView.setVisibility(View.VISIBLE);
            emptyPantryTextView.setVisibility(View.GONE);
        }
    }


    /*
     * Called when the Edit button for an
     * individual pantry item is clicked.
     */
    @Override
    public void onEditClick(PantryItem pantryItem) {

        Intent intent =
                new Intent(
                        ViewPantryActivity.this,
                        EditPantryItemActivity.class
                );

        intent.putExtra(
                "PANTRY_ID",
                pantryItem.getId()
        );

        intent.putExtra(
                "PANTRY_NAME",
                pantryItem.getName()
        );

        intent.putExtra(
                "PANTRY_QUANTITY",
                pantryItem.getQuantity()
        );

        intent.putExtra(
                "PANTRY_UNIT",
                pantryItem.getUnit()
        );

        intent.putExtra(
                "PANTRY_EXPIRY",
                pantryItem.getExpiryDate()
        );

        startActivity(intent);
    }


    /*
     * Called when the Delete button for an
     * individual pantry item is clicked.
     */
    @Override
    public void onDeleteClick(PantryItem pantryItem) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Pantry Item")
                .setMessage(
                        "Are you sure you want to delete "
                                + pantryItem.getName()
                                + "?"
                )

                .setPositiveButton("Delete", (dialog, which) -> {

                    int result =
                            databaseHelper.deletePantryItem(
                                    pantryItem.getId()
                            );

                    if (result > 0) {

                        Toast.makeText(
                                this,
                                pantryItem.getName()
                                        + " deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Reload the list
                        loadPantryItems();

                    } else {

                        Toast.makeText(
                                this,
                                "Unable to delete item",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })

                .setNegativeButton("Cancel", null)

                .show();
    }


    /*
     * Reload the pantry whenever the user
     * returns to this activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadPantryItems();
        }
    }
}
