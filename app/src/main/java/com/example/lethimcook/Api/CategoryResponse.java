package com.example.lethimcook.Api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("categories")
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public static class Category {
        @SerializedName("strCategory")
        private String name;

        public String getName() {
            return name;
        }
    }
}