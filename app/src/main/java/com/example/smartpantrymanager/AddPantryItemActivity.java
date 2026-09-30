package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class AddPantryItemActivity extends AppCompatActivity {

    // Input layouts
    private TextInputLayout nameLayout;
    private TextInputLayout quantityLayout;
    private TextInputLayout unitLayout;
    private TextInputLayout expiryDateLayout;

    // Input fields
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputEditText unitInput;
    private TextInputEditText expiryDateInput;

    // Buttons
    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    // Toolbar
    private MaterialToolbar toolbar;

    // Database
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_pantry_item);

        // Find input layouts
        nameLayout = findViewById(R.id.layout_pantry_name);
        quantityLayout = findViewById(R.id.layout_pantry_quantity);
        unitLayout = findViewById(R.id.layout_pantry_unit);
        expiryDateLayout = findViewById(R.id.layout_expiry_date);

        // Find input fields
        nameInput = findViewById(R.id.input_pantry_name);
        quantityInput = findViewById(R.id.input_pantry_quantity);
        unitInput = findViewById(R.id.input_pantry_unit);
        expiryDateInput = findViewById(R.id.input_expiry_date);

        // Find buttons
        saveButton = findViewById(R.id.button_save_pantry_item);
        cancelButton = findViewById(R.id.button_cancel_pantry_item);

        // Find toolbar
        toolbar = findViewById(R.id.add_pantry_toolbar);

        // Create database helper
        databaseHelper = new DatabaseHelper(this);


        // Toolbar back button
        toolbar.setNavigationOnClickListener(view -> finish());


        // Expiry date field
        expiryDateInput.setOnClickListener(view -> showDatePicker());


        // Save pantry item
        saveButton.setOnClickListener(view -> savePantryItem());


        // Cancel
        cancelButton.setOnClickListener(view -> finish());
    }


    /*
     * Opens a DatePickerDialog and places
     * the selected date into the expiry field.
     */
    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            String selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%04d-%02d-%02d",
                                            selectedYear,
                                            selectedMonth + 1,
                                            selectedDay
                                    );

                            expiryDateInput.setText(selectedDate);

                            expiryDateLayout.setError(null);
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }


    /*
     * Validates the form and inserts the item
     * into the SQLite database.
     */
    private void savePantryItem() {

        clearErrors();

        String name = getText(nameInput);
        String quantityText = getText(quantityInput);
        String unit = getText(unitInput);
        String expiryDate = getText(expiryDateInput);


        boolean isValid = true;


        // Validate name
        if (name.isEmpty()) {

            nameLayout.setError("Enter an item name");
            isValid = false;
        }


        // Validate quantity
        if (quantityText.isEmpty()) {

            quantityLayout.setError("Enter a quantity");
            isValid = false;
        }


        // Validate unit
        if (unit.isEmpty()) {

            unitLayout.setError("Enter a unit");
            isValid = false;
        }


        // Validate expiry date
        if (expiryDate.isEmpty()) {

            expiryDateLayout.setError("Select an expiry date");
            isValid = false;
        }


        if (!isValid) {
            return;
        }


        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            quantityLayout.setError("Enter a valid quantity");
            return;
        }


        if (quantity <= 0) {

            quantityLayout.setError(
                    "Quantity must be greater than 0"
            );

            return;
        }


        // Create PantryItem object
        PantryItem pantryItem =
                new PantryItem(
                        0,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );


        // Add item to SQLite database
        long result =
                databaseHelper.addPantryItem(pantryItem);


        if (result != -1) {

            Toast.makeText(
                    this,
                    "Pantry item added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            // Return to ViewPantryActivity
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Unable to add pantry item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    /*
     * Returns the text from a TextInputEditText
     * without leading or trailing spaces.
     */
    private String getText(TextInputEditText input) {

        if (input.getText() == null) {
            return "";
        }

        return input
                .getText()
                .toString()
                .trim();
    }


    /*
     * Clears previous validation errors.
     */
    private void clearErrors() {

        nameLayout.setError(null);
        quantityLayout.setError(null);
        unitLayout.setError(null);
        expiryDateLayout.setError(null);
    }
}