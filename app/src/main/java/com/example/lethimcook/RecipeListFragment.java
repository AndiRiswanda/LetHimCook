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
//import com.facebook.shimmer.ShimmerFrameLayout; // optional for shimmer
import com.google.android.material.button.MaterialButton;

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

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If you want to pass args, do it here
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

        // Example: search for “chicken” (you can parameterize later)
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
                        adapter = new MealAdapter(meals, RecipeListFragment.this);
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
        // For simplicity, show a Toast with meal name, category, area
        String info = meal.getName() + " (" + meal.getCategory() + " - " + meal.getArea() + ")";
        Toast.makeText(getContext(), info, Toast.LENGTH_LONG).show();
    }
}
