package com.example.v4pantryplanner;

// Import necessary Android and Java libraries
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.v4pantryplanner.DatabaseHelper;
import com.example.v4pantryplanner.SelectRecipe;

import java.util.ArrayList;
import java.util.List;

public class FindRecipe extends AppCompatActivity {

    // Spinners for selecting ingredients
    private Spinner ingredient1Spinner, ingredient2Spinner, ingredient3Spinner;
    private DatabaseHelper databaseHelper; // Helper class for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe); // Set the layout for the activity

        // Link UI elements (spinners and button) to their corresponding views in the layout
        ingredient1Spinner = findViewById(R.id.ingredient1_spinner);
        ingredient2Spinner = findViewById(R.id.ingredient2_spinner);
        ingredient3Spinner = findViewById(R.id.ingredient3_spinner);
        Button searchButton = findViewById(R.id.search_button);

        databaseHelper = new DatabaseHelper(this); // Initialize the database helper

        setupSpinners(); // Populate the spinners with ingredient data from the database

        // Set up the button click listener to search for recipes
        searchButton.setOnClickListener(v -> searchForRecipe());
    }

    /**
     * Configures the ingredient selection spinners by populating them with data
     * fetched from the database.
     */
    private void setupSpinners() {
        // Fetch all ingredients from the database
        List<String> ingredients = databaseHelper.getAllIngredients();

        // Create an ArrayAdapter to display the ingredients in a dropdown menu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinners
        ingredient1Spinner.setAdapter(adapter);
        ingredient2Spinner.setAdapter(adapter);
        ingredient3Spinner.setAdapter(adapter);
    }

    /**
     * Handles the recipe search logic based on the selected ingredients.
     * If all ingredients are selected, it launches the SelectRecipe activity.
     */
    private void searchForRecipe() {
        // Get the selected ingredients from the spinners
        String ingredient1 = (String) ingredient1Spinner.getSelectedItem();
        String ingredient2 = (String) ingredient2Spinner.getSelectedItem();
        String ingredient3 = (String) ingredient3Spinner.getSelectedItem();

        // Ensure all spinners have valid selections
        if (ingredient1 == null || ingredient2 == null || ingredient3 == null) {
            // Display a message if any spinner is left unselected
            Toast.makeText(this, "Please select all ingredients", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an intent to navigate to the SelectRecipe activity
        Intent intent = new Intent(this, SelectRecipe.class);

        // Pass the selected ingredients as extras in the intent
        intent.putExtra("ingredient1", ingredient1);
        intent.putExtra("ingredient2", ingredient2);
        intent.putExtra("ingredient3", ingredient3);

        // Start the SelectRecipe activity
        startActivity(intent);
    }
}
