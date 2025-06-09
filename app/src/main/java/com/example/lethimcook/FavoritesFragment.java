package com.example.lethimcook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lethimcook.Adapter.MealAdapter;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.db.FavoritesDbHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements MealAdapter.OnMealClickListener {

    private RecyclerView rvFavorites;
    private TextView tvEmpty;
    private MealAdapter adapter;
    private FavoritesDbHelper dbHelper;
    private List<Meal> favoritesList = new ArrayList<>();

    public FavoritesFragment() {
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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmpty = view.findViewById(R.id.tvEmptyFavorites);

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load favorite meals
        loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload favorites when coming back to this fragment
        loadFavorites();
    }

    private void loadFavorites() {
        favoritesList = dbHelper.getAllFavorites();

        if (favoritesList.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            adapter = new MealAdapter(favoritesList, this, dbHelper);
            rvFavorites.setAdapter(adapter);
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        RecipeDetail.start(requireContext(), meal.getIdMeal());
    }

    @Override
    public void onFavoriteToggle(Meal meal, boolean isFavorite) {
        if (!isFavorite) {
            // Remove from favorites
            dbHelper.removeFavorite(meal.getIdMeal());
            Snackbar.make(requireView(), "Removed from favorites", Snackbar.LENGTH_SHORT).show();

            // Refresh the list
            loadFavorites();
        }
    }
}