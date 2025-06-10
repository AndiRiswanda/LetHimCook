package com.example.lethimcook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Model.Ingredient;
import com.google.android.material.appbar.MaterialToolbar;

public class SpiceDetailActivity extends AppCompatActivity {

    private static final String EXTRA_INGREDIENT_ID = "ingredient_id";
    private static final String EXTRA_INGREDIENT_NAME = "ingredient_name";
    private static final String EXTRA_INGREDIENT_DESCRIPTION = "ingredient_description";
    private static final String EXTRA_INGREDIENT_IMAGE_URL = "ingredient_image_url";
    private static final String EXTRA_INGREDIENT_IMAGE_RES = "ingredient_image_res";

    private ImageView imgIngredient;
    private TextView tvIngredientName, tvIngredientDescription;

    public static void start(Context context, Ingredient ingredient) {
        Intent intent = new Intent(context, SpiceDetailActivity.class);
        intent.putExtra(EXTRA_INGREDIENT_ID, ingredient.getId());
        intent.putExtra(EXTRA_INGREDIENT_NAME, ingredient.getName());
        intent.putExtra(EXTRA_INGREDIENT_DESCRIPTION, ingredient.getDescription());
        intent.putExtra(EXTRA_INGREDIENT_IMAGE_URL, ingredient.getImageUrl());
        intent.putExtra(EXTRA_INGREDIENT_IMAGE_RES, ingredient.getImageResId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_detail);

        // Initialize views
        initViews();

        // Configure toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Handle window insets
        setupWindowInsets();

        // Load ingredient details
        loadIngredientDetails();
    }

    private void initViews() {
        imgIngredient = findViewById(R.id.imgIngredient);
        tvIngredientName = findViewById(R.id.tvIngredientName);
        tvIngredientDescription = findViewById(R.id.tvIngredientDescription);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), insets.top, v.getPaddingRight(), v.getPaddingBottom());
            return windowInsets;
        });
    }

    private void loadIngredientDetails() {
        // Get data from intent
        String name = getIntent().getStringExtra(EXTRA_INGREDIENT_NAME);
        String description = getIntent().getStringExtra(EXTRA_INGREDIENT_DESCRIPTION);
        String imageUrl = getIntent().getStringExtra(EXTRA_INGREDIENT_IMAGE_URL);
        int imageRes = getIntent().getIntExtra(EXTRA_INGREDIENT_IMAGE_RES, R.drawable.placeholder_ingredient);

        // Set ingredient name
        tvIngredientName.setText(name);

        // Set ingredient description
        if (description != null && !description.isEmpty()) {
            tvIngredientDescription.setText(description);
        } else {
            tvIngredientDescription.setText("description_available");
        }

        // Load image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // If we have a URL, use it and remove "-Small" to get the large image
            String largeImageUrl = imageUrl.replace("-Small", "");
            Glide.with(this)
                    .load(largeImageUrl)
                    .placeholder(R.drawable.placeholder_ingredient)
                    .error(R.drawable.placeholder_ingredient)
                    .into(imgIngredient);
        } else if (imageRes != 0) {
            // Use local resource as fallback
            imgIngredient.setImageResource(imageRes);
        } else {
            // Use placeholder as last resort
            imgIngredient.setImageResource(R.drawable.placeholder_ingredient);
        }
    }
}