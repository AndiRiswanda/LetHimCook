package com.example.lethimcook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lethimcook.Adapter.IngredientAdapter;
import com.example.lethimcook.Model.Ingredient;
import com.example.lethimcook.db.IngredientRepository;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class SpiceIdentifierActivity extends AppCompatActivity implements IngredientAdapter.OnIngredientClickListener {

    private RecyclerView rvIngredients;
    private ProgressBar progressBar;
    private TextView tvError;
    private EditText etIngredientSearch;
    private ImageButton btnClearSearch;

    private IngredientRepository repository;
    private IngredientAdapter adapter;
    private List<Ingredient> allIngredients = new ArrayList<>();
    private List<Ingredient> filteredIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_identifier);

        // Initialize views
        rvIngredients = findViewById(R.id.rvIngredients);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        etIngredientSearch = findViewById(R.id.etIngredientSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerView
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(filteredIngredients, this);
        rvIngredients.setAdapter(adapter);


        // Initialize repository
        repository = new IngredientRepository();

        // Setup search functionality
        setupSearch();

        // Load ingredients
        loadIngredients();
    }

    private void setupSearch() {
        etIngredientSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                filterIngredients(query);
                btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearSearch.setOnClickListener(v -> {
            etIngredientSearch.setText("");
            btnClearSearch.setVisibility(View.GONE);
        });
    }

    private void filterIngredients(String query) {
        filteredIngredients.clear();

        if (query.isEmpty()) {
            filteredIngredients.addAll(allIngredients);
        } else {
            for (Ingredient ingredient : allIngredients) {
                if (ingredient.getName().toLowerCase().contains(query)) {
                    filteredIngredients.add(ingredient);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void loadIngredients() {
        showLoading();

        repository.getAllIngredients(new IngredientRepository.LoadIngredientsCallback() {
            @Override
            public void onIngredientsLoaded(List<Ingredient> ingredientList) {
                allIngredients.clear();
                allIngredients.addAll(ingredientList);

                filteredIngredients.clear();
                filteredIngredients.addAll(ingredientList);

                adapter.notifyDataSetChanged();
                showContent();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvIngredients.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        rvIngredients.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        rvIngredients.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        SpiceDetailActivity.start(this, ingredient);
    }
}