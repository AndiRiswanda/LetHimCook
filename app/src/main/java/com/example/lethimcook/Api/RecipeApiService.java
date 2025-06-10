package com.example.lethimcook.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    @GET("lookup.php")
    Call<MealDetailResponse> getMealById(@Query("i") String id);

    // Keep your other methods unchanged
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("search.php")
    Call<MealResponse> searchMealsByName(@Query("s") String name);

    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    @GET("list.php?i=list")
    Call<IngredientListResponse> getAllIngredients();
}