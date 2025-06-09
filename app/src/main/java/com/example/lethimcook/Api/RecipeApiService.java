package com.example.lethimcook.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    // Example: https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String name);

    // e.g., list by category: https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    // e.g., random meal
    @GET("random.php")
    Call<MealResponse> getRandomMeal();
}