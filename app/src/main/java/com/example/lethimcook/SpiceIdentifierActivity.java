package com.example.lethimcook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lethimcook.Adapter.IngredientAdapter;
import com.example.lethimcook.Model.Ingredient;
import com.example.lethimcook.db.IngredientRepository;

public class SpiceIdentifierActivity extends AppCompatActivity
        implements IngredientAdapter.OnIngredientClickListener {

    private RecyclerView rvIngredients;
    private IngredientRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spice_identifier);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Set up toolbar arrow (back)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_spice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvIngredients = findViewById(R.id.rvIngredients);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));

        repository = new IngredientRepository(this);
        loadIngredients();
    }

    private void loadIngredients() {
        repository.getAllIngredients(ingredients -> {
            if (ingredients == null || ingredients.isEmpty()) {
                Toast.makeText(this, "No ingredients found.", Toast.LENGTH_SHORT).show();
                return;
            }
            IngredientAdapter adapter = new IngredientAdapter(ingredients, this);
            rvIngredients.setAdapter(adapter);
        });
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        Intent intent = new Intent(this, SpiceDetailActivity.class);
        intent.putExtra("ING_ID", ingredient.getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}