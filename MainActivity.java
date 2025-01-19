package com.example.v4pantryplanner;

// Import necessary Android and Java libraries
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        // Link the button in the UI to its corresponding view in the layout
        Button enterButton = findViewById(R.id.enterButton);

        // Set up a click listener for the button
        enterButton.setOnClickListener(v -> {
            // Create an intent to navigate to the FindRecipe activity
            Intent intent = new Intent(MainActivity.this, FindRecipe.class);
            startActivity(intent); // Start the FindRecipe activity
        });
    }
}
