package com.example.lethimcook.Model;

public class IngredientMeasure {
    private String ingredient;
    private String measure;
    private String imageUrl;

    public IngredientMeasure(String ingredient, String measure, String imageUrl) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.imageUrl = imageUrl;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Helper method to convert to Ingredient object (optional)
    public Ingredient toIngredient() {
        return new Ingredient(
                0, // No specific ID
                ingredient,
                "", // No description available
                0,  // No local resource
                imageUrl
        );
    }
}