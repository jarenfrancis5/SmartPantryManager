package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry";

    // Pantry columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createPantryTable);
    }


    // Called when the database version is increased
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);

        onCreate(db);
    }


    // Add a pantry item
    public long addPantryItem(PantryItem pantryItem) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, pantryItem.getName());
        values.put(COLUMN_QUANTITY, pantryItem.getQuantity());
        values.put(COLUMN_UNIT, pantryItem.getUnit());
        values.put(COLUMN_EXPIRY_DATE, pantryItem.getExpiryDate());

        long result = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return result;
    }


    // Get all pantry items
    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );


                PantryItem pantryItem = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(pantryItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }


    // Update a pantry item
    public int updatePantryItem(PantryItem pantryItem) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, pantryItem.getName());
        values.put(COLUMN_QUANTITY, pantryItem.getQuantity());
        values.put(COLUMN_UNIT, pantryItem.getUnit());
        values.put(COLUMN_EXPIRY_DATE, pantryItem.getExpiryDate());

        int rowsAffected = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(pantryItem.getId())}
        );

        db.close();

        return rowsAffected;
    }


    // Delete a pantry item
    public int deletePantryItem(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return rowsAffected;
    }
}
