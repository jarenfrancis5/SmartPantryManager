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

public class EditPantryItemActivity extends AppCompatActivity {

    private TextInputLayout nameLayout;
    private TextInputLayout quantityLayout;
    private TextInputLayout unitLayout;
    private TextInputLayout expiryDateLayout;

    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputEditText unitInput;
    private TextInputEditText expiryDateInput;

    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    private MaterialToolbar toolbar;

    private DatabaseHelper databaseHelper;

    // ID of the pantry item being edited
    private int pantryItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reuse the Add Pantry layout
        setContentView(R.layout.activity_add_pantry_item);

        // Find layouts
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

        databaseHelper = new DatabaseHelper(this);

        // Change text so the screen looks like an Edit screen
        toolbar.setTitle("Edit Pantry Item");
        saveButton.setText("Update Pantry Item");

        toolbar.setNavigationOnClickListener(view -> finish());

        // Get selected pantry item information from Intent
        pantryItemId = getIntent().getIntExtra("PANTRY_ID", -1);

        String name =
                getIntent().getStringExtra("PANTRY_NAME");

        double quantity =
                getIntent().getDoubleExtra(
                        "PANTRY_QUANTITY",
                        0
                );

        String unit =
                getIntent().getStringExtra("PANTRY_UNIT");

        String expiryDate =
                getIntent().getStringExtra(
                        "PANTRY_EXPIRY"
                );

        // Put current values into the fields
        nameInput.setText(name);

        quantityInput.setText(
                String.valueOf(quantity)
        );

        unitInput.setText(unit);

        expiryDateInput.setText(expiryDate);


        // Date picker
        expiryDateInput.setOnClickListener(
                view -> showDatePicker()
        );


        // Update pantry item
        saveButton.setOnClickListener(
                view -> updatePantryItem()
        );


        // Cancel
        cancelButton.setOnClickListener(
                view -> finish()
        );
    }


    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view,
                         selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            String selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%04d-%02d-%02d",
                                            selectedYear,
                                            selectedMonth + 1,
                                            selectedDay
                                    );

                            expiryDateInput.setText(
                                    selectedDate
                            );

                            expiryDateLayout.setError(
                                    null
                            );
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }


    private void updatePantryItem() {

        clearErrors();

        String name =
                getText(nameInput);

        String quantityText =
                getText(quantityInput);

        String unit =
                getText(unitInput);

        String expiryDate =
                getText(expiryDateInput);


        boolean isValid = true;


        if (name.isEmpty()) {

            nameLayout.setError(
                    "Enter an item name"
            );

            isValid = false;
        }


        if (quantityText.isEmpty()) {

            quantityLayout.setError(
                    "Enter a quantity"
            );

            isValid = false;
        }


        if (unit.isEmpty()) {

            unitLayout.setError(
                    "Enter a unit"
            );

            isValid = false;
        }


        if (expiryDate.isEmpty()) {

            expiryDateLayout.setError(
                    "Select an expiry date"
            );

            isValid = false;
        }


        if (!isValid) {
            return;
        }


        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            quantityLayout.setError(
                    "Enter a valid quantity"
            );

            return;
        }


        if (quantity <= 0) {

            quantityLayout.setError(
                    "Quantity must be greater than 0"
            );

            return;
        }


        PantryItem pantryItem =
                new PantryItem(
                        pantryItemId,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );


        int rowsAffected =
                databaseHelper.updatePantryItem(
                        pantryItem
                );


        if (rowsAffected > 0) {

            Toast.makeText(
                    this,
                    "Pantry item updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Unable to update pantry item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    private String getText(
            TextInputEditText input
    ) {

        if (input.getText() == null) {
            return "";
        }

        return input
                .getText()
                .toString()
                .trim();
    }


    private void clearErrors() {

        nameLayout.setError(null);
        quantityLayout.setError(null);
        unitLayout.setError(null);
        expiryDateLayout.setError(null);
    }
}
