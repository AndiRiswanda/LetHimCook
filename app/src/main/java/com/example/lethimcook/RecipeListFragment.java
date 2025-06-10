package com.example.lethimcook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lethimcook.Adapter.MealAdapter;
import com.example.lethimcook.Api.CategoryResponse;
import com.example.lethimcook.Api.MealResponse;
import com.example.lethimcook.Api.RetrofitClient;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.db.FavoritesDbHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment implements MealAdapter.OnMealClickListener {

    private RecyclerView rvMeals;
    private ProgressBar progressBar;
    private TextView tvError;
    private ChipGroup categoryChipGroup;
    private SearchView searchView;
    private MaterialButton btnLoadMore;

    private MealAdapter adapter;
    private List<Meal> mealList = new ArrayList<>();
    private FavoritesDbHelper dbHelper;
    private String currentCategory = ""; // empty means All
    private boolean isSearching = false;
    private int currentPage = 1;
    private static final int MEALS_PER_PAGE = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FavoritesDbHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        rvMeals = view.findViewById(R.id.rvMeals);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);
        categoryChipGroup = view.findViewById(R.id.categoryChipGroup);
        searchView = view.findViewById(R.id.searchView);
        btnLoadMore = view.findViewById(R.id.btnLoadMore);

        setupRecyclerView();
        setupSearch();
        loadCategories();

        btnLoadMore.setOnClickListener(v -> loadMoreMeals());

        return view;
    }

    private void setupRecyclerView() {
        rvMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealAdapter(mealList, this, dbHelper);
        rvMeals.setAdapter(adapter);

        // Add scroll listener to detect when user reaches bottom
        rvMeals.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Show load more button when reaching close to the bottom
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3
                        && firstVisibleItemPosition >= 0) {
                    btnLoadMore.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    isSearching = true;
                    searchMeals(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    // If search is cleared, go back to current category
                    if (isSearching) {
                        isSearching = false;
                        currentPage = 1;
                        loadMealsByCategory(currentCategory);
                    }
                }
                return true;
            }
        });
    }

    private void loadCategories() {
        progressBar.setVisibility(View.VISIBLE);

        Call<CategoryResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .getCategories();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupCategoryChips(response.body().getCategories());

                    // Load random meals initially (for "All" category)
                    loadRandomMeals();
                } else {
                    showError("Failed to load categories");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void setupCategoryChips(List<CategoryResponse.Category> categories) {
        // Add "All" chip first
        Chip allChip = createChip("All");
        allChip.setChecked(true);
        categoryChipGroup.addView(allChip);

        // Add other category chips
        for (CategoryResponse.Category category : categories) {
            Chip chip = createChip(category.getName());
            categoryChipGroup.addView(chip);
        }

        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String category = chip.getText().toString();
                if ("All".equals(category)) {
                    currentCategory = "";
                } else {
                    currentCategory = category;
                }
                currentPage = 1;
                isSearching = false;
                loadMealsByCategory(currentCategory);
            }
        });
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setChipBackgroundColorResource(R.color.chip_background_color_selector);
        return chip;
    }

    private void loadRandomMeals() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnLoadMore.setVisibility(View.GONE);

        // Create a list to hold all the meals we're going to load
        List<Meal> randomMeals = new ArrayList<>();
        final int totalToLoad = MEALS_PER_PAGE;
        final int[] loadedCount = {0};

        // Load multiple random meals
        for (int i = 0; i < totalToLoad; i++) {
            Call<MealResponse> call = RetrofitClient.getInstance()
                    .getApiService()
                    .getRandomMeal();

            final int index = i;
            call.enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                    loadedCount[0]++;

                    if (response.isSuccessful() && response.body() != null &&
                            response.body().getMeals() != null &&
                            !response.body().getMeals().isEmpty()) {
                        Meal meal = response.body().getMeals().get(0);

                        // Make sure we don't add duplicates
                        boolean isDuplicate = false;
                        for (Meal existingMeal : randomMeals) {
                            if (existingMeal.getIdMeal().equals(meal.getIdMeal())) {
                                isDuplicate = true;
                                break;
                            }
                        }

                        if (!isDuplicate) {
                            randomMeals.add(meal);
                        }
                    }

                    // If this is the last callback, update the UI
                    if (loadedCount[0] >= totalToLoad) {
                        updateMealList(randomMeals, true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                    loadedCount[0]++;

                    // If this is the last callback, update the UI even if this specific call failed
                    if (loadedCount[0] >= totalToLoad) {
                        updateMealList(randomMeals, true);
                    }
                }
            });
        }
    }

    private void loadMealsByCategory(String category) {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnLoadMore.setVisibility(View.GONE);

        if (category.isEmpty()) {
            // "All" category selected, load random meals
            loadRandomMeals();
            return;
        }

        Call<MealResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .getMealsByCategory(category);

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getMeals() != null) {

                    List<Meal> meals = response.body().getMeals();

                    // Shuffle the meals for randomness
                    Collections.shuffle(meals);

                    // Take only first batch of meals
                    if (meals.size() > MEALS_PER_PAGE) {
                        List<Meal> batchMeals = meals.subList(0, MEALS_PER_PAGE);
                        updateMealList(batchMeals, true);

                        // Show load more button if there are more meals
                        btnLoadMore.setVisibility(View.VISIBLE);
                    } else {
                        updateMealList(meals, true);
                        btnLoadMore.setVisibility(View.GONE);
                    }
                } else {
                    showError("No meals found for this category");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void searchMeals(String query) {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnLoadMore.setVisibility(View.GONE);

        Call<MealResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .searchMealsByName(query);

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getMeals() != null) {
                    updateMealList(response.body().getMeals(), true);
                    btnLoadMore.setVisibility(View.GONE);
                } else {
                    showError("No meals found for your search");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void loadMoreMeals() {
        if (isSearching || !currentCategory.isEmpty()) {
            // For categories, we already have all meals, just show more
            if (!currentCategory.isEmpty()) {
                currentPage++;

                Call<MealResponse> call = RetrofitClient.getInstance()
                        .getApiService()
                        .getMealsByCategory(currentCategory);

                call.enqueue(new Callback<MealResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                        if (response.isSuccessful() && response.body() != null &&
                                response.body().getMeals() != null) {

                            List<Meal> allMeals = response.body().getMeals();
                            Collections.shuffle(allMeals);

                            int startIndex = (currentPage - 1) * MEALS_PER_PAGE;
                            if (startIndex < allMeals.size()) {
                                int endIndex = Math.min(startIndex + MEALS_PER_PAGE, allMeals.size());
                                List<Meal> nextBatch = allMeals.subList(startIndex, endIndex);

                                // Add to existing list
                                updateMealList(nextBatch, false);

                                // Hide load more button if we've shown all meals
                                if (endIndex >= allMeals.size()) {
                                    btnLoadMore.setVisibility(View.GONE);
                                }
                            } else {
                                btnLoadMore.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                        // Keep the button visible so user can try again
                    }
                });
            }
        } else {
            // For "All" category, load more random meals
            loadMoreRandomMeals();
        }
    }

    private void loadMoreRandomMeals() {
        btnLoadMore.setEnabled(false);

        // Create a list to hold all the meals we're going to load
        List<Meal> randomMeals = new ArrayList<>();
        final int totalToLoad = MEALS_PER_PAGE;
        final int[] loadedCount = {0};

        // Load multiple random meals
        for (int i = 0; i < totalToLoad; i++) {
            Call<MealResponse> call = RetrofitClient.getInstance()
                    .getApiService()
                    .getRandomMeal();

            call.enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                    loadedCount[0]++;

                    if (response.isSuccessful() && response.body() != null &&
                            response.body().getMeals() != null &&
                            !response.body().getMeals().isEmpty()) {
                        Meal meal = response.body().getMeals().get(0);

                        // Make sure we don't add duplicates
                        boolean isDuplicate = false;
                        for (Meal existingMeal : mealList) {
                            if (existingMeal.getIdMeal().equals(meal.getIdMeal())) {
                                isDuplicate = true;
                                break;
                            }
                        }

                        if (!isDuplicate) {
                            randomMeals.add(meal);
                        }
                    }

                    // If this is the last callback, update the UI
                    if (loadedCount[0] >= totalToLoad) {
                        updateMealList(randomMeals, false);
                        btnLoadMore.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                    loadedCount[0]++;

                    // If this is the last callback, update the UI even if this specific call failed
                    if (loadedCount[0] >= totalToLoad) {
                        updateMealList(randomMeals, false);
                        btnLoadMore.setEnabled(true);
                    }
                }
            });
        }
    }

    private void updateMealList(List<Meal> meals, boolean replaceExisting) {
        progressBar.setVisibility(View.GONE);

        if (meals.isEmpty()) {
            if (replaceExisting) {
                mealList.clear();
                adapter.notifyDataSetChanged();
                showError("No meals found");
            }
            return;
        }

        if (replaceExisting) {
            mealList.clear();
        }

        mealList.addAll(meals);
        adapter.notifyDataSetChanged();
        tvError.setVisibility(View.GONE);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the adapter to update favorite status
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        // Navigate to recipe detail screen
        RecipeDetail.start(requireContext(), meal.getIdMeal());
    }

    @Override
    public void onFavoriteToggle(Meal meal, boolean isFavorite) {
        // This is handled by the adapter
    }
}