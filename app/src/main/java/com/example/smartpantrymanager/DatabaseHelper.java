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
    private static final int DATABASE_VERSION = 2;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry";

    // Pantry columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";


    // Recipes table
    private static final String TABLE_RECIPES = "recipes";

    // Recipes columns
    private static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_PREPARATION_STEPS = "preparation_steps";


    // Recipe ingredients table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Recipe ingredient columns
    private static final String COLUMN_INGREDIENT_ID = "ingredient_id";
    private static final String COLUMN_INGREDIENT_RECIPE_ID = "recipe_id";
    private static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    private static final String COLUMN_REQUIRED_QUANTITY = "required_quantity";
    private static final String COLUMN_INGREDIENT_UNIT = "unit";


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create pantry table
        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createPantryTable);


        // Create recipes table
        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        COLUMN_PREPARATION_STEPS + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipesTable);


        // Create recipe ingredients table
        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COLUMN_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                        COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COLUMN_REQUIRED_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_INGREDIENT_RECIPE_ID + ") " +
                        "REFERENCES " + TABLE_RECIPES +
                        "(" + COLUMN_RECIPE_ID + ") " +
                        "ON DELETE CASCADE" +
                        ")";

        db.execSQL(createRecipeIngredientsTable);


        // Insert default recipes
        seedRecipes(db);
    }



    // Called when the database version is increased
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
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


    public long addRecipe(Recipe recipe) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                recipe.getRecipeName()
        );

        values.put(
                COLUMN_PREPARATION_STEPS,
                recipe.getPreparationSteps()
        );

        long recipeId = db.insert(
                TABLE_RECIPES,
                null,
                values
        );

        db.close();

        return recipeId;
    }

    public long addRecipeIngredient(
            long recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                COLUMN_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                COLUMN_REQUIRED_QUANTITY,
                requiredQuantity
        );

        values.put(
                COLUMN_INGREDIENT_UNIT,
                unit
        );

        long result = db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );

        db.close();

        return result;
    }

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int recipeId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_ID
                        )
                );

                String recipeName = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_NAME
                        )
                );

                String preparationSteps = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_PREPARATION_STEPS
                        )
                );

                Recipe recipe = new Recipe(
                        recipeId,
                        recipeName,
                        preparationSteps
                );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                COLUMN_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            COLUMN_RECIPE_ID
                    )
            );

            String recipeName = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            COLUMN_RECIPE_NAME
                    )
            );

            String preparationSteps = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            COLUMN_PREPARATION_STEPS
                    )
            );

            recipe = new Recipe(
                    id,
                    recipeName,
                    preparationSteps
            );
        }

        cursor.close();
        db.close();

        return recipe;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId) {

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COLUMN_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                COLUMN_INGREDIENT_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int ingredientId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_INGREDIENT_ID
                        )
                );

                int linkedRecipeId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_INGREDIENT_RECIPE_ID
                        )
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_INGREDIENT_NAME
                        )
                );

                double requiredQuantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_REQUIRED_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_INGREDIENT_UNIT
                        )
                );

                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                ingredientId,
                                linkedRecipeId,
                                ingredientName,
                                requiredQuantity,
                                unit
                        );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredients;
    }

    private long insertRecipe(
            SQLiteDatabase db,
            String recipeName,
            String preparationSteps) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPE_NAME, recipeName);
        values.put(COLUMN_PREPARATION_STEPS, preparationSteps);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    private void insertRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                COLUMN_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                COLUMN_REQUIRED_QUANTITY,
                requiredQuantity
        );

        values.put(
                COLUMN_INGREDIENT_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }

    private void seedRecipes(SQLiteDatabase db) {

        // ------------------------------------------------
        // 1. Chicken Pasta
        // ------------------------------------------------

        long chickenPastaId = insertRecipe(
                db,
                "Chicken Pasta",
                "1. Boil the pasta until cooked.\n" +
                        "2. Cut the chicken into small pieces.\n" +
                        "3. Cook the chicken in a pan.\n" +
                        "4. Add chopped tomatoes and onion.\n" +
                        "5. Add the cooked pasta and mix well."
        );

        insertRecipeIngredient(
                db,
                chickenPastaId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                chickenPastaId,
                "Chicken",
                250,
                "g"
        );

        insertRecipeIngredient(
                db,
                chickenPastaId,
                "Tomato",
                2,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                chickenPastaId,
                "Onion",
                1,
                "piece"
        );


        // ------------------------------------------------
        // 2. Scrambled Eggs
        // ------------------------------------------------

        long scrambledEggsId = insertRecipe(
                db,
                "Scrambled Eggs",
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add the milk and whisk together.\n" +
                        "3. Melt butter in a pan.\n" +
                        "4. Add the egg mixture.\n" +
                        "5. Stir gently until the eggs are cooked."
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Eggs",
                3,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Milk",
                50,
                "ml"
        );

        insertRecipeIngredient(
                db,
                scrambledEggsId,
                "Butter",
                10,
                "g"
        );


        // ------------------------------------------------
        // 3. Tomato Sandwich
        // ------------------------------------------------

        long tomatoSandwichId = insertRecipe(
                db,
                "Tomato Sandwich",
                "1. Slice the tomato.\n" +
                        "2. Spread butter on the bread.\n" +
                        "3. Place tomato slices on one slice of bread.\n" +
                        "4. Cover with the second slice."
        );

        insertRecipeIngredient(
                db,
                tomatoSandwichId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                tomatoSandwichId,
                "Tomato",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                tomatoSandwichId,
                "Butter",
                10,
                "g"
        );


        // ------------------------------------------------
        // 4. Cheese Omelette
        // ------------------------------------------------

        long cheeseOmeletteId = insertRecipe(
                db,
                "Cheese Omelette",
                "1. Beat the eggs in a bowl.\n" +
                        "2. Heat butter in a pan.\n" +
                        "3. Pour in the eggs.\n" +
                        "4. Add grated cheese.\n" +
                        "5. Fold the omelette and cook until ready."
        );

        insertRecipeIngredient(
                db,
                cheeseOmeletteId,
                "Eggs",
                2,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                cheeseOmeletteId,
                "Cheese",
                50,
                "g"
        );

        insertRecipeIngredient(
                db,
                cheeseOmeletteId,
                "Butter",
                10,
                "g"
        );


        // ------------------------------------------------
        // 5. Tuna Sandwich
        // ------------------------------------------------

        long tunaSandwichId = insertRecipe(
                db,
                "Tuna Sandwich",
                "1. Drain the tuna.\n" +
                        "2. Mix the tuna with mayonnaise.\n" +
                        "3. Place the mixture onto the bread.\n" +
                        "4. Cover with another slice of bread."
        );

        insertRecipeIngredient(
                db,
                tunaSandwichId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                tunaSandwichId,
                "Tuna",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                tunaSandwichId,
                "Mayonnaise",
                20,
                "g"
        );


        // ------------------------------------------------
        // 6. Banana Smoothie
        // ------------------------------------------------

        long bananaSmoothieId = insertRecipe(
                db,
                "Banana Smoothie",
                "1. Peel the banana.\n" +
                        "2. Add banana and milk to a blender.\n" +
                        "3. Blend until smooth.\n" +
                        "4. Pour into a glass and serve."
        );

        insertRecipeIngredient(
                db,
                bananaSmoothieId,
                "Banana",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                bananaSmoothieId,
                "Milk",
                250,
                "ml"
        );


        // ------------------------------------------------
        // 7. Grilled Cheese Sandwich
        // ------------------------------------------------

        long grilledCheeseId = insertRecipe(
                db,
                "Grilled Cheese Sandwich",
                "1. Butter the bread slices.\n" +
                        "2. Place cheese between the slices.\n" +
                        "3. Grill in a pan until golden brown.\n" +
                        "4. Turn over and cook the other side."
        );

        insertRecipeIngredient(
                db,
                grilledCheeseId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                grilledCheeseId,
                "Cheese",
                60,
                "g"
        );

        insertRecipeIngredient(
                db,
                grilledCheeseId,
                "Butter",
                10,
                "g"
        );


        // ------------------------------------------------
        // 8. Vegetable Stir Fry
        // ------------------------------------------------

        long vegetableStirFryId = insertRecipe(
                db,
                "Vegetable Stir Fry",
                "1. Chop all vegetables.\n" +
                        "2. Heat oil in a pan.\n" +
                        "3. Add onion and cook briefly.\n" +
                        "4. Add carrots and peppers.\n" +
                        "5. Stir-fry until the vegetables are tender."
        );

        insertRecipeIngredient(
                db,
                vegetableStirFryId,
                "Carrot",
                2,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                vegetableStirFryId,
                "Bell Pepper",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                vegetableStirFryId,
                "Onion",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                vegetableStirFryId,
                "Cooking Oil",
                15,
                "ml"
        );


        // ------------------------------------------------
        // 9. Chicken Sandwich
        // ------------------------------------------------

        long chickenSandwichId = insertRecipe(
                db,
                "Chicken Sandwich",
                "1. Cook and slice the chicken.\n" +
                        "2. Place chicken onto the bread.\n" +
                        "3. Add tomato and lettuce.\n" +
                        "4. Cover with the second slice of bread."
        );

        insertRecipeIngredient(
                db,
                chickenSandwichId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                chickenSandwichId,
                "Chicken",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                chickenSandwichId,
                "Tomato",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                chickenSandwichId,
                "Lettuce",
                2,
                "leaves"
        );


        // ------------------------------------------------
        // 10. Tomato Pasta
        // ------------------------------------------------

        long tomatoPastaId = insertRecipe(
                db,
                "Tomato Pasta",
                "1. Boil the pasta.\n" +
                        "2. Chop the tomatoes and onion.\n" +
                        "3. Cook onion and tomato in a pan.\n" +
                        "4. Add the cooked pasta.\n" +
                        "5. Mix and serve."
        );

        insertRecipeIngredient(
                db,
                tomatoPastaId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                tomatoPastaId,
                "Tomato",
                3,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                tomatoPastaId,
                "Onion",
                1,
                "piece"
        );


        // ------------------------------------------------
        // 11. French Toast
        // ------------------------------------------------

        long frenchToastId = insertRecipe(
                db,
                "French Toast",
                "1. Beat the eggs and milk together.\n" +
                        "2. Dip bread into the mixture.\n" +
                        "3. Melt butter in a pan.\n" +
                        "4. Fry each slice until golden brown."
        );

        insertRecipeIngredient(
                db,
                frenchToastId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                frenchToastId,
                "Eggs",
                2,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                frenchToastId,
                "Milk",
                100,
                "ml"
        );

        insertRecipeIngredient(
                db,
                frenchToastId,
                "Butter",
                10,
                "g"
        );


        // ------------------------------------------------
        // 12. Mashed Potatoes
        // ------------------------------------------------

        long mashedPotatoesId = insertRecipe(
                db,
                "Mashed Potatoes",
                "1. Peel and chop the potatoes.\n" +
                        "2. Boil until soft.\n" +
                        "3. Drain the water.\n" +
                        "4. Add milk and butter.\n" +
                        "5. Mash until smooth."
        );

        insertRecipeIngredient(
                db,
                mashedPotatoesId,
                "Potato",
                4,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                mashedPotatoesId,
                "Milk",
                100,
                "ml"
        );

        insertRecipeIngredient(
                db,
                mashedPotatoesId,
                "Butter",
                20,
                "g"
        );


        // ------------------------------------------------
        // 13. Egg Sandwich
        // ------------------------------------------------

        long eggSandwichId = insertRecipe(
                db,
                "Egg Sandwich",
                "1. Boil the eggs.\n" +
                        "2. Peel and mash the eggs.\n" +
                        "3. Mix with mayonnaise.\n" +
                        "4. Spread onto bread and serve."
        );

        insertRecipeIngredient(
                db,
                eggSandwichId,
                "Bread",
                2,
                "slices"
        );

        insertRecipeIngredient(
                db,
                eggSandwichId,
                "Eggs",
                2,
                "pieces"
        );

        insertRecipeIngredient(
                db,
                eggSandwichId,
                "Mayonnaise",
                20,
                "g"
        );


        // ------------------------------------------------
        // 14. Chicken Rice
        // ------------------------------------------------

        long chickenRiceId = insertRecipe(
                db,
                "Chicken Rice",
                "1. Cook the rice.\n" +
                        "2. Cut and cook the chicken.\n" +
                        "3. Chop and cook the onion.\n" +
                        "4. Add chicken and rice together.\n" +
                        "5. Mix and serve."
        );

        insertRecipeIngredient(
                db,
                chickenRiceId,
                "Rice",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                chickenRiceId,
                "Chicken",
                250,
                "g"
        );

        insertRecipeIngredient(
                db,
                chickenRiceId,
                "Onion",
                1,
                "piece"
        );


        // ------------------------------------------------
        // 15. Fruit Salad
        // ------------------------------------------------

        long fruitSaladId = insertRecipe(
                db,
                "Fruit Salad",
                "1. Wash the fruit.\n" +
                        "2. Peel the banana and orange.\n" +
                        "3. Chop all fruit into small pieces.\n" +
                        "4. Mix together in a bowl."
        );

        insertRecipeIngredient(
                db,
                fruitSaladId,
                "Banana",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                fruitSaladId,
                "Apple",
                1,
                "piece"
        );

        insertRecipeIngredient(
                db,
                fruitSaladId,
                "Orange",
                1,
                "piece"
        );
    }
}
