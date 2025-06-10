package com.example.lethimcook.Model;

public class CookingMistake {
    private int id;
    private String name;
    private String description;
    private String category;
    private int imageResId;
    private String imageUrl;

    public CookingMistake(int id, String name, String description, String category, int imageResId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
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

    public String getCategory() {
        return category;
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