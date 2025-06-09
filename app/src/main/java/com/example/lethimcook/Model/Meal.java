package com.example.lethimcook.Model;
import com.google.gson.annotations.SerializedName;
public class Meal {
    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String thumbnailUrl;

    @SerializedName("strYoutube")
    private String youtubeUrl;

    // We could add more fields (ingredients, measures), but for simplicity, we’ll omit.

    // Getters:

    public String getIdMeal() {
        return idMeal;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}
