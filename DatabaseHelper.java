package com.example.v4pantryplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    public static final String DATABASE_NAME = "RecipesDB";
    public static final int DATABASE_VERSION = 2; // Incremented version

    // Table and column names
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMAGE = "image"; // Image column to store resource ID
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_METHOD = "method";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create recipes table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_IMAGE + " INTEGER NOT NULL, " // Store resource ID
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_INGREDIENTS + " TEXT NOT NULL, "
                + COLUMN_METHOD + " TEXT NOT NULL)";
        db.execSQL(CREATE_RECIPES_TABLE);

        // Insert sample data
        addSampleRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new recipe for Tomato Soup in version 2
            addRecipe(db, R.drawable.tomato_soup, "Tomato Soup", "Tomato, Onion, Cream", "Boil tomatoes and onions, blend, and add cream.");
        }
    }

    // Add sample recipes to the database
    private void addSampleRecipes(SQLiteDatabase db) {
        addRecipe(db, R.drawable.tomato_salad, "Tomato Salad", "Tomato, Onion, Cheese", "Mix all ingredients and serve chilled.");
        addRecipe(db, R.drawable.cheese_toast, "Cheese Toast", "Cheese, Onion, Garlic", "Toast bread, add toppings, and grill.");
        addRecipe(db, R.drawable.garlic_bread, "Garlic Bread", "Garlic, Butter, Bread", "Spread butter and garlic, then bake.");
        addRecipe(db, R.drawable.tomato_soup, "Tomato Soup", "Tomato, Onion, Cream", "Boil tomatoes and onions, blend, and add cream.");
    }

    // Add a single recipe to the database
    private void addRecipe(SQLiteDatabase db, int imageResId, String name, String ingredients, String method) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imageResId); // Store image resource ID
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_INGREDIENTS, ingredients);
        values.put(COLUMN_METHOD, method);
        db.insert(TABLE_RECIPES, null, values);
    }

    // Fetch recipes matching ingredients
    public Cursor getRecipesByIngredients(String[] ingredients) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Build query to match all ingredients
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < ingredients.length; i++) {
            selection.append(COLUMN_INGREDIENTS).append(" LIKE ?");
            if (i < ingredients.length - 1) {
                selection.append(" AND ");
            }
        }

        String[] selectionArgs = new String[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            selectionArgs[i] = "%" + ingredients[i] + "%";
        }

        return db.query(TABLE_RECIPES, null, selection.toString(), selectionArgs, null, null, null);
    }

    // Fetch all unique ingredients
    public List<String> getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_INGREDIENTS + " FROM " + TABLE_RECIPES, null);

        List<String> uniqueIngredients = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            int ingredientsColumnIndex = cursor.getColumnIndex(COLUMN_INGREDIENTS);
            if (ingredientsColumnIndex >= 0) {
                do {
                    String ingredientData = cursor.getString(ingredientsColumnIndex);
                    String[] splitIngredients = ingredientData.split(", ");
                    for (String ingredient : splitIngredients) {
                        if (!uniqueIngredients.contains(ingredient.trim())) {
                            uniqueIngredients.add(ingredient.trim());
                        }
                    }
                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return uniqueIngredients;
    }
}
