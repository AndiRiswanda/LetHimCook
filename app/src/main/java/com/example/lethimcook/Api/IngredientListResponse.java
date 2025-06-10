package com.example.lethimcook.Api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IngredientListResponse {
    @SerializedName("meals")
    private List<ApiIngredient> ingredients;

    public List<ApiIngredient> getIngredients() {
        return ingredients;
    }

    public static class ApiIngredient {
        @SerializedName("idIngredient")
        private String id;

        @SerializedName("strIngredient")
        private String name;

        @SerializedName("strDescription")
        private String description;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}