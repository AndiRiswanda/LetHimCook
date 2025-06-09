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

    // In Meal.java, add these fields and getters/setters
    @SerializedName("strIngredient1") private String ingredient1;
    @SerializedName("strIngredient2") private String ingredient2;
    @SerializedName("strIngredient3") private String ingredient3;
    @SerializedName("strIngredient4") private String ingredient4;
    @SerializedName("strIngredient5") private String ingredient5;
    @SerializedName("strIngredient6") private String ingredient6;
    @SerializedName("strIngredient7") private String ingredient7;
    @SerializedName("strIngredient8") private String ingredient8;
    @SerializedName("strIngredient9") private String ingredient9;
    @SerializedName("strIngredient10") private String ingredient10;
    @SerializedName("strIngredient11") private String ingredient11;
    @SerializedName("strIngredient12") private String ingredient12;
    @SerializedName("strIngredient13") private String ingredient13;
    @SerializedName("strIngredient14") private String ingredient14;
    @SerializedName("strIngredient15") private String ingredient15;
    @SerializedName("strIngredient16") private String ingredient16;
    @SerializedName("strIngredient17") private String ingredient17;
    @SerializedName("strIngredient18") private String ingredient18;
    @SerializedName("strIngredient19") private String ingredient19;
    @SerializedName("strIngredient20") private String ingredient20;

    @SerializedName("strMeasure1") private String measure1;
    @SerializedName("strMeasure2") private String measure2;
    @SerializedName("strMeasure3") private String measure3;
    @SerializedName("strMeasure4") private String measure4;
    @SerializedName("strMeasure5") private String measure5;
    @SerializedName("strMeasure6") private String measure6;
    @SerializedName("strMeasure7") private String measure7;
    @SerializedName("strMeasure8") private String measure8;
    @SerializedName("strMeasure9") private String measure9;
    @SerializedName("strMeasure10") private String measure10;
    @SerializedName("strMeasure11") private String measure11;
    @SerializedName("strMeasure12") private String measure12;
    @SerializedName("strMeasure13") private String measure13;
    @SerializedName("strMeasure14") private String measure14;
    @SerializedName("strMeasure15") private String measure15;
    @SerializedName("strMeasure16") private String measure16;
    @SerializedName("strMeasure17") private String measure17;
    @SerializedName("strMeasure18") private String measure18;
    @SerializedName("strMeasure19") private String measure19;
    @SerializedName("strMeasure20") private String measure20;

    public String getIngredient(int index) {
        switch (index) {
            case 1: return ingredient1;
            case 2: return ingredient2;
            case 3: return ingredient3;
            case 4: return ingredient4;
            case 5: return ingredient5;
            case 6: return ingredient6;
            case 7: return ingredient7;
            case 8: return ingredient8;
            case 9: return ingredient9;
            case 10: return ingredient10;
            case 11: return ingredient11;
            case 12: return ingredient12;
            case 13: return ingredient13;
            case 14: return ingredient14;
            case 15: return ingredient15;
            case 16: return ingredient16;
            case 17: return ingredient17;
            case 18: return ingredient18;
            case 19: return ingredient19;
            case 20: return ingredient20;
            default: return null;
        }
    }

    public String getMeasure(int index) {
        switch (index) {
            case 1: return measure1;
            case 2: return measure2;
            case 3: return measure3;
            case 4: return measure4;
            case 5: return measure5;
            case 6: return measure6;
            case 7: return measure7;
            case 8: return measure8;
            case 9: return measure9;
            case 10: return measure10;
            case 11: return measure11;
            case 12: return measure12;
            case 13: return measure13;
            case 14: return measure14;
            case 15: return measure15;
            case 16: return measure16;
            case 17: return measure17;
            case 18: return measure18;
            case 19: return measure19;
            case 20: return measure20;
            default: return null;
        }
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

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

    // Add these methods to your Meal class
    public void setIngredient(int index, String ingredient) {
        switch (index) {
            case 1: ingredient1 = ingredient; break;
            case 2: ingredient2 = ingredient; break;
            case 3: ingredient3 = ingredient; break;
            case 4: ingredient4 = ingredient; break;
            case 5: ingredient5 = ingredient; break;
            case 6: ingredient6 = ingredient; break;
            case 7: ingredient7 = ingredient; break;
            case 8: ingredient8 = ingredient; break;
            case 9: ingredient9 = ingredient; break;
            case 10: ingredient10 = ingredient; break;
            case 11: ingredient11 = ingredient; break;
            case 12: ingredient12 = ingredient; break;
            case 13: ingredient13 = ingredient; break;
            case 14: ingredient14 = ingredient; break;
            case 15: ingredient15 = ingredient; break;
            case 16: ingredient16 = ingredient; break;
            case 17: ingredient17 = ingredient; break;
            case 18: ingredient18 = ingredient; break;
            case 19: ingredient19 = ingredient; break;
            case 20: ingredient20 = ingredient; break;
        }
    }

    public void setMeasure(int index, String measure) {
        switch (index) {
            case 1: measure1 = measure; break;
            case 2: measure2 = measure; break;
            case 3: measure3 = measure; break;
            case 4: measure4 = measure; break;
            case 5: measure5 = measure; break;
            case 6: measure6 = measure; break;
            case 7: measure7 = measure; break;
            case 8: measure8 = measure; break;
            case 9: measure9 = measure; break;
            case 10: measure10 = measure; break;
            case 11: measure11 = measure; break;
            case 12: measure12 = measure; break;
            case 13: measure13 = measure; break;
            case 14: measure14 = measure; break;
            case 15: measure15 = measure; break;
            case 16: measure16 = measure; break;
            case 17: measure17 = measure; break;
            case 18: measure18 = measure; break;
            case 19: measure19 = measure; break;
            case 20: measure20 = measure; break;
        }
    }
}
