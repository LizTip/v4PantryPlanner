package com.example.v4pantryplanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectRecipe extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipe);

        // Initialize RecyclerView
        recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get ingredients from Intent
        String ingredient1 = getIntent().getStringExtra("ingredient1");
        String ingredient2 = getIntent().getStringExtra("ingredient2");
        String ingredient3 = getIntent().getStringExtra("ingredient3");

        // Check if ingredients are available
        if (ingredient1 != null && ingredient2 != null && ingredient3 != null) {
            new LoadRecipesTask().execute(ingredient1, ingredient2, ingredient3);
        } else {
            Toast.makeText(this, "Error: Missing ingredient data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class LoadRecipesTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... ingredients) {
            // Fetch recipes matching the selected ingredients
            return databaseHelper.getRecipesByIngredients(ingredients);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                recipeAdapter = new RecipeAdapter(
                        SelectRecipe.this, cursor,
                        (name, ingredients, method, imageResId) -> {
                            // Navigate to Recipe page
                            Intent intent = new Intent(SelectRecipe.this, Recipe.class);
                            intent.putExtra("recipe_name", name);
                            intent.putExtra("recipe_ingredients", ingredients);
                            intent.putExtra("recipe_method", method);
                            intent.putExtra("recipe_image", imageResId); // Pass the image resource ID
                            startActivity(intent);
                        });
                recipeRecyclerView.setAdapter(recipeAdapter);
            } else {
                // No recipes found, show toast message
                Toast.makeText(SelectRecipe.this, "No recipe found, try selecting different ingredients", Toast.LENGTH_SHORT).show();
                finish(); // Close the SelectRecipe activity and return to the FindRecipe screen
            }
        }
    }
}
