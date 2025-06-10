package com.example.lethimcook.Model;

public class Ingredient {
    private int id;
    private String name;
    private String description;
    private int imageResId; // Local drawable resource ID (for fallback)
    private String imageUrl; // URL from TheMealDB

    public Ingredient(int id, String name, String description, int imageResId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
    }

    // Original constructor for backward compatibility
    public Ingredient(int id, String name, String description, int imageResId) {
        this(id, name, description, imageResId, null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean hasImageUrl() {
        return imageUrl != null && !imageUrl.isEmpty();
    }
}