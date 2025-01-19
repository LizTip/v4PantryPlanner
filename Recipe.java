package com.example.v4pantryplanner;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Recipe extends AppCompatActivity {

    private RecyclerView ingredientsRecyclerView;
    private RecipeDetailsAdapter recipeDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Get recipe details from Intent
        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeIngredients = getIntent().getStringExtra("recipe_ingredients");
        String recipeMethod = getIntent().getStringExtra("recipe_method");
        int recipeImageResId = getIntent().getIntExtra("recipe_image", -1);

        // Set recipe image
        ImageView recipeImage = findViewById(R.id.recipe_image);
        if (recipeImageResId != -1) {
            recipeImage.setImageResource(recipeImageResId);
        }

        // Set recipe title
        TextView recipeTitle = findViewById(R.id.recipe_title);
        recipeTitle.setText(recipeName);

        // Set up RecyclerView for ingredients and method
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepare data for RecyclerView
        List<String> recipeDetails = new ArrayList<>();
        recipeDetails.add("Ingredients:\n" + recipeIngredients);
        recipeDetails.add("Method:\n" + recipeMethod);

        // Set up adapter
        recipeDetailsAdapter = new RecipeDetailsAdapter(recipeDetails);
        ingredientsRecyclerView.setAdapter(recipeDetailsAdapter);
    }
}