package com.example.lethimcook.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.lethimcook.Api.IngredientListResponse;
import com.example.lethimcook.Api.RetrofitClient;
import com.example.lethimcook.Model.Ingredient;
import com.example.lethimcook.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientRepository {
    private static final String TAG = "IngredientRepository";
    private Handler mainHandler;

    public interface LoadIngredientsCallback {
        void onIngredientsLoaded(List<Ingredient> ingredientList);
        void onError(String message);
    }

    public IngredientRepository() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getAllIngredients(final LoadIngredientsCallback callback) {
        Call<IngredientListResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .getAllIngredients();

        call.enqueue(new Callback<IngredientListResponse>() {
            @Override
            public void onResponse(Call<IngredientListResponse> call, Response<IngredientListResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getIngredients() != null) {

                    List<Ingredient> ingredients = new ArrayList<>();
                    List<IngredientListResponse.ApiIngredient> apiIngredients = response.body().getIngredients();

                    for (IngredientListResponse.ApiIngredient apiIngredient : apiIngredients) {
                        // Convert API ingredient to your model
                        String id = apiIngredient.getId();
                        String name = apiIngredient.getName();
                        String description = apiIngredient.getDescription();

                        if (name != null && !name.isEmpty()) {
                            // Generate image URL for the ingredient
                            String imageUrl = "https://www.themealdb.com/images/ingredients/" +
                                    name.replace(" ", "%20") + ".png";

                            // Create and add the ingredient
                            // Use R.drawable.placeholder_ingredient as a placeholder
                            Ingredient ingredient = new Ingredient(
                                    Integer.parseInt(id),
                                    name,
                                    description != null ? description : "",
                                    R.drawable.placeholder_ingredient,
                                    imageUrl
                            );

                            ingredients.add(ingredient);
                        }
                    }

                    mainHandler.post(() -> callback.onIngredientsLoaded(ingredients));
                } else {
                    mainHandler.post(() -> callback.onError("Failed to load ingredients"));
                }
            }

            @Override
            public void onFailure(Call<IngredientListResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                mainHandler.post(() -> callback.onError("Network error: " + t.getMessage()));
            }
        });
    }
}