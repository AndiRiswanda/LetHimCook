package com.example.lethimcook;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lethimcook.Adapter.CookingMistakesAdapter;
import com.example.lethimcook.Model.CookingMistake;
import com.example.lethimcook.db.MistakesDbHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class CommonMistakesActivity extends AppCompatActivity implements CookingMistakesAdapter.OnMistakeClickListener {

    private RecyclerView rvMistakes;
    private ChipGroup categoryChipGroup;
    private TextView tvEmptyState;
    private View progressBar;

    private MistakesDbHelper dbHelper;
    private CookingMistakesAdapter adapter;
    private List<CookingMistake> mistakesList = new ArrayList<>();
    private String currentCategory = null; // null means all categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_mistakes);

        // Initialize database helper
        dbHelper = new MistakesDbHelper(this);

        // Setup UI
        setupUI();

        // Setup toolbar with navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup adapter
        adapter = new CookingMistakesAdapter(mistakesList, this);
        rvMistakes.setAdapter(adapter);

        // Load categories and mistakes
        loadCategories();
        loadMistakes(null, 10); // Start with all categories, 10 per category
    }

    private void setupUI() {
        rvMistakes = findViewById(R.id.rvMistakes);
        categoryChipGroup = findViewById(R.id.categoryChipGroup);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        progressBar = findViewById(R.id.progressBar);

        rvMistakes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadCategories() {
        List<String> categories = dbHelper.getAllCategories();

        // Add "All" chip
        Chip allChip = new Chip(this);
        allChip.setText("All");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setChipBackgroundColorResource(R.color.chip_background_color_selector);
        categoryChipGroup.addView(allChip);

        // Add chip for each category
        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.chip_background_color_selector);
            categoryChipGroup.addView(chip);
        }

        // Set listener for chip selection
        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == View.NO_ID) {
                // If nothing is selected, select "All"
                allChip.setChecked(true);
                return;
            }

            Chip selectedChip = findViewById(checkedId);
            String category = selectedChip.getText().toString();
            currentCategory = "All".equals(category) ? null : category;
            loadMistakes(currentCategory, 10);
        });
    }

    private void loadMistakes(String category, int limit) {
        progressBar.setVisibility(View.VISIBLE);
        rvMistakes.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        // Load mistakes from database
        List<CookingMistake> mistakes = dbHelper.getMistakesByCategory(category, limit);

        progressBar.setVisibility(View.GONE);

        if (mistakes.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvMistakes.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvMistakes.setVisibility(View.VISIBLE);
            mistakesList.clear();
            mistakesList.addAll(mistakes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMistakeClick(CookingMistake mistake) {
//        // Show detailed view of the mistake
//        MistakeDetailActivity.start(this, mistake);
    }
}