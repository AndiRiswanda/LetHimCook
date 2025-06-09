package com.example.lethimcook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lethimcook.Adapter.MealAdapter;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.Api.MealResponse;
import com.example.lethimcook.Api.RetrofitClient;
import com.example.lethimcook.db.FavoritesDbHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment implements MealAdapter.OnMealClickListener {

    private RecyclerView rvMeals;
    private ProgressBar progressBar;
    private MaterialButton btnRefresh;
    private MealAdapter adapter;
    private FavoritesDbHelper dbHelper;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FavoritesDbHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMeals = view.findViewById(R.id.rvMeals);
        progressBar = view.findViewById(R.id.progressBar);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        rvMeals.setLayoutManager(new LinearLayoutManager(getContext()));

        btnRefresh.setOnClickListener(v -> fetchMeals());

        fetchMeals();
    }

    private void fetchMeals() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);
        rvMeals.setVisibility(View.GONE);

        // Example: search for "chicken" (you can parameterize later)
        Call<MealResponse> call = RetrofitClient.getInstance()
                .getApiService()
                .searchMeals("chicken");

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        adapter = new MealAdapter(meals, RecipeListFragment.this, dbHelper);
                        rvMeals.setAdapter(adapter);
                        rvMeals.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "No recipes found.", Toast.LENGTH_SHORT).show();
                        btnRefresh.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "API error.", Toast.LENGTH_SHORT).show();
                    btnRefresh.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                btnRefresh.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMealClick(Meal meal) {
        // Navigate to RecipeDetail activity
        RecipeDetail.start(requireContext(), meal.getIdMeal());
    }

    @Override
    public void onFavoriteToggle(Meal meal, boolean isFavorite) {
        if (isFavorite) {
            // Add to favorites
            long result = dbHelper.addFavorite(meal);
            if (result > 0) {
                Snackbar.make(requireView(), "Added to favorites", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(requireView(), "Error adding to favorites", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            // Remove from favorites
            boolean removed = dbHelper.removeFavorite(meal.getIdMeal());
            if (removed) {
                Snackbar.make(requireView(), "Removed from favorites", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh favorite status in the adapter when coming back to this fragment
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // This will cause all items to check their favorite status again
        }
    }
}