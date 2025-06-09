package com.example.lethimcook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Adapter.IngredientMeasureAdapter;
import com.example.lethimcook.Api.MealDetailResponse;
import com.example.lethimcook.Api.RetrofitClient;
import com.example.lethimcook.Model.IngredientMeasure;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.db.FavoritesDbHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetail extends AppCompatActivity {

    private static final String EXTRA_MEAL_ID = "meal_id";

    // UI elements
    private View progressBar;
    private AppBarLayout appBarLayout;
    private NestedScrollView contentScrollView;
    private ImageView imgRecipeHeader;
    private TextView tvRecipeTitle, tvInstructions;
    private Chip chipCategory, chipArea;
    private MaterialButton btnFavorite, btnYoutube;
    private RecyclerView rvIngredients;

    // Data
    private String mealId;
    private Meal meal;
    private FavoritesDbHelper dbHelper;
    private IngredientMeasureAdapter ingredientAdapter;

    public static void start(Context context, String mealId) {
        Intent intent = new Intent(context, RecipeDetail.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Initialize database helper
        dbHelper = new FavoritesDbHelper(this);

        // Get meal ID from intent
        mealId = getIntent().getStringExtra(EXTRA_MEAL_ID);
        if (mealId == null) {
            Toast.makeText(this, "Recipe ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup ingredients recyclerView
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new IngredientMeasureAdapter(new ArrayList<>());
        rvIngredients.setAdapter(ingredientAdapter);

        // Load meal details
        loadMealDetails();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        appBarLayout = findViewById(R.id.appBarLayout);
        contentScrollView = findViewById(R.id.contentScrollView);
        imgRecipeHeader = findViewById(R.id.imgRecipeHeader);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvInstructions = findViewById(R.id.tvInstructions);
        chipCategory = findViewById(R.id.chipCategory);
        chipArea = findViewById(R.id.chipArea);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnYoutube = findViewById(R.id.btnYoutube);
        rvIngredients = findViewById(R.id.rvIngredients);
    }

    private void loadMealDetails() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.GONE);
        contentScrollView.setVisibility(View.GONE);

        // Try to load from database first
        Meal offlineMeal = dbHelper.getFavoriteMealById(mealId);
        if (offlineMeal != null) {
            // Meal exists in database, display it
            meal = offlineMeal;
            displayMealDetails(meal);

            // Hide loading, show content
            progressBar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
            contentScrollView.setVisibility(View.VISIBLE);
            return;
        }

        // If not in database, make API call
        Call<MealDetailResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .getMealById(mealId);

        call.enqueue(new Callback<MealDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealDetailResponse> call,
                                   @NonNull Response<MealDetailResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null
                        && response.body().getMeals() != null
                        && !response.body().getMeals().isEmpty()) {

                    meal = response.body().getMeals().get(0);
                    displayMealDetails(meal);

                    // Show content
                    appBarLayout.setVisibility(View.VISIBLE);
                    contentScrollView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(RecipeDetail.this,
                            "Failed to load recipe details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealDetailResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecipeDetail.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayMealDetails(Meal meal) {
        Glide.with(this)
                .load(meal.getThumbnailUrl())
                .placeholder(R.drawable.placeholder_ingredient)
                .error(R.drawable.placeholder_ingredient) // Show placeholder if image can't be loaded
                .into(imgRecipeHeader);
//        // Set header image
//        Glide.with(this)
//                .load(meal.getThumbnailUrl())
//                .placeholder(R.drawable.placeholder_ingredient)
//                .into(imgRecipeHeader);

        // Set basic info
        tvRecipeTitle.setText(meal.getName());
        tvInstructions.setText(meal.getInstructions());
        chipCategory.setText(meal.getCategory());
        chipArea.setText(meal.getArea());

        // Set favorite button state
        updateFavoriteButtonState();

        // Setup favorite button listener
        btnFavorite.setOnClickListener(v -> toggleFavorite());

        // Setup YouTube button
        String youtubeUrl = meal.getYoutubeUrl();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            btnYoutube.setVisibility(View.VISIBLE);
            btnYoutube.setOnClickListener(v -> openYoutubeLink(youtubeUrl));
        } else {
            btnYoutube.setVisibility(View.GONE);
        }

        // Load and display ingredients
        loadIngredients();
    }

    private void loadIngredients() {
        List<IngredientMeasure> ingredientList = new ArrayList<>();

        // Extract ingredients and measures from the meal object
        for (int i = 1; i <= 20; i++) {
            String ingredient = meal.getIngredient(i);
            String measure = meal.getMeasure(i);

            if (ingredient != null && !ingredient.trim().isEmpty()
                    && !ingredient.equals("null")) {
                // Generate image URL for the ingredient
                // TheMealDB API provides ingredient images at this URL pattern
                String imageUrl = "https://www.themealdb.com/images/ingredients/" +
                        ingredient.replace(" ", "%20") + "-Small.png";

                ingredientList.add(new IngredientMeasure(ingredient, measure, imageUrl));
            }
        }

        // Update the adapter with the ingredients
        ingredientAdapter.updateIngredients(ingredientList);
    }

    private void updateFavoriteButtonState() {
        boolean isFavorite = dbHelper.isFavorite(mealId);

        if (isFavorite) {
            btnFavorite.setIconTint(getColorStateList(R.color.favorite_active));
            btnFavorite.setIcon(getDrawable(R.drawable.baseline_favorite_24));
        } else {
            btnFavorite.setIconTint(getColorStateList(R.color.favorite_inactive));
            btnFavorite.setIcon(getDrawable(R.drawable.baseline_favorite_border_24));
        }
    }

    private void toggleFavorite() {
        boolean isFavorite = dbHelper.isFavorite(mealId);

        if (isFavorite) {
            // Remove from favorites
            dbHelper.removeFavorite(mealId);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Add to favorites
            long result = dbHelper.addFavorite(meal);
            if (result > 0) {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
            }
        }

        // Update button state
        updateFavoriteButtonState();
    }

    private void openYoutubeLink(String youtubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youtubeUrl));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No app found to open the video link",
                    Toast.LENGTH_SHORT).show();
        }
    }
}