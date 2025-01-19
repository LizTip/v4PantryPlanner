package com.example.v4pantryplanner;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private Cursor cursor;
    private final OnRecipeClickListener onRecipeClickListener;

    // Interface for handling recipe clicks
    public interface OnRecipeClickListener {
        void onRecipeClick(String name, String ingredients, String method, int imageResId);
    }

    // Constructor with OnRecipeClickListener
    public RecipeAdapter(Context context, Cursor cursor, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.cursor = cursor;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            // Retrieve column indices
            int imageColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE);
            int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int ingredientsColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_INGREDIENTS);
            int methodColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_METHOD);

            // Retrieve data from cursor
            int imageResId = (imageColumnIndex != -1) ? cursor.getInt(imageColumnIndex) : 0;
            String recipeName = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
            String recipeIngredients = (ingredientsColumnIndex != -1) ? cursor.getString(ingredientsColumnIndex) : "";
            String recipeMethod = (methodColumnIndex != -1) ? cursor.getString(methodColumnIndex) : "";

            // Bind data to the ViewHolder
            holder.recipeImageView.setImageResource(imageResId);
            holder.recipeNameTextView.setText(recipeName);
            holder.recipeIngredientsTextView.setText(recipeIngredients);

            // Handle clicks on the recipe
            holder.itemView.setOnClickListener(v -> {
                if (onRecipeClickListener != null) {
                    onRecipeClickListener.onRecipeClick(recipeName, recipeIngredients, recipeMethod, imageResId);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImageView; // For displaying recipe image
        TextView recipeNameTextView;
        TextView recipeIngredientsTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipe_image); // Bind ImageView
            recipeNameTextView = itemView.findViewById(R.id.recipe_name); // Bind Name TextView
            recipeIngredientsTextView = itemView.findViewById(R.id.recipe_ingredients); // Bind Ingredients TextView
        }
    }
}