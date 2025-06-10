package com.example.lethimcook;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Api.RetrofitClient;
import com.example.lethimcook.Model.Ingredient;
import com.example.lethimcook.db.IngredientRepository;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IngredientQuizActivity extends AppCompatActivity {
    private LinearLayout errorLayout;
    private TextView tvErrorMessage;
    private Button btnErrorRefresh;
    private TextView tvQuestion, tvScore, tvFeedback;
    private ImageView imgIngredient;
    private Button btnOption1, btnOption2;
    private MaterialCardView cardFeedback;
    private LinearProgressIndicator progressIndicator;
    private MaterialCardView imageCard;
    private List<Ingredient> allIngredients = new ArrayList<>();
    private int currentScore = 0;
    private int totalQuestions = 0;
    private int correctAnswerIndex;
    private Ingredient correctIngredient;
    private Ingredient wrongIngredient;
    private Handler handler = new Handler();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_quiz);

        initViews();
        setupToolbar();
        loadIngredients();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvFeedback = findViewById(R.id.tvFeedback);
        imgIngredient = findViewById(R.id.imgIngredient);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        cardFeedback = findViewById(R.id.cardFeedback);
        progressIndicator = findViewById(R.id.progressIndicator);
        errorLayout = findViewById(R.id.errorLayout);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnErrorRefresh = findViewById(R.id.btnErrorRefresh);
        imageCard = findViewById(R.id.imageCard);


        btnOption1.setOnClickListener(v -> checkAnswer(0));
        btnOption2.setOnClickListener(v -> checkAnswer(1));
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadIngredients() {
        showLoading();
        IngredientRepository repository = new IngredientRepository();

        repository.getAllIngredients(new IngredientRepository.LoadIngredientsCallback() {
            @Override
            public void onIngredientsLoaded(List<Ingredient> ingredientList) {
                allIngredients.clear();
                allIngredients.addAll(ingredientList);
                progressIndicator.setVisibility(View.GONE);

                if (allIngredients.size() >= 2) {
                    showContent();
                    generateQuestion();
                } else {
                    tvQuestion.setText("Not enough ingredients loaded");
                }
            }
            @Override
            public void onError(String message) {
                showError("Error loading ingredients: " + message);
            }
        });
    }

    private void generateQuestion() {
        cardFeedback.setVisibility(View.GONE);
        enableButtons(true);

        // Select two random ingredients
        Collections.shuffle(allIngredients);
        correctIngredient = allIngredients.get(0);
        wrongIngredient = allIngredients.get(1);

        // Randomly decide which is the correct answer
        correctAnswerIndex = random.nextInt(2);

        // Load the image of the correct ingredient
        Glide.with(this)
                .load(correctIngredient.getImageUrl())
                .placeholder(R.drawable.placeholder_ingredient)
                .error(R.drawable.placeholder_ingredient)
                .into(imgIngredient);

        // Set up the question
        tvQuestion.setText("Which ingredient is this?");

        // Set button texts based on randomized position
        if (correctAnswerIndex == 0) {
            btnOption1.setText(correctIngredient.getName());
            btnOption2.setText(wrongIngredient.getName());
        } else {
            btnOption1.setText(wrongIngredient.getName());
            btnOption2.setText(correctIngredient.getName());
        }

        totalQuestions++;
        updateScoreDisplay();
    }

    private void checkAnswer(int selectedIndex) {
        enableButtons(false);

        if (selectedIndex == correctAnswerIndex) {
            // Correct answer
            currentScore++;
            cardFeedback.setCardBackgroundColor(getResources().getColor(R.color.secondary_container, null));
            tvFeedback.setText("Correct! This is " + correctIngredient.getName());
        } else {
            // Wrong answer
            cardFeedback.setCardBackgroundColor(getResources().getColor(R.color.error_container, null));
            tvFeedback.setText("Wrong! This is " + correctIngredient.getName());
        }

        cardFeedback.setVisibility(View.VISIBLE);
        updateScoreDisplay();

        // Wait 2 seconds before next question
        handler.postDelayed(this::generateQuestion, 2000);
    }

    private void enableButtons(boolean enable) {
        btnOption1.setEnabled(enable);
        btnOption2.setEnabled(enable);
    }

    private void updateScoreDisplay() {
        tvScore.setText(String.format("Score: %d/%d", currentScore, totalQuestions));
    }


    private void showLoading() {
        progressIndicator.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        // Hide other views when loading
        imgIngredient.setVisibility(View.GONE);
        btnOption1.setVisibility(View.GONE);
        btnOption2.setVisibility(View.GONE);
        cardFeedback.setVisibility(View.GONE);
    }

    private void showContent() {
        progressIndicator.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        // Show quiz content
        imgIngredient.setVisibility(View.VISIBLE);
        btnOption1.setVisibility(View.VISIBLE);
        btnOption2.setVisibility(View.VISIBLE);
        tvQuestion.setVisibility(View.VISIBLE);
        imageCard.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        progressIndicator.setVisibility(View.GONE);
        // Hide quiz content
        imgIngredient.setVisibility(View.GONE);
        btnOption1.setVisibility(View.GONE);
        btnOption2.setVisibility(View.GONE);
        cardFeedback.setVisibility(View.GONE);
        tvQuestion.setVisibility(View.GONE);
        imageCard.setVisibility(View.GONE);

        // Show error layout
        errorLayout.setVisibility(View.VISIBLE);
        tvErrorMessage.setText(message);

        // Ensure the refresh button is visible and has a click listener
        btnErrorRefresh.setVisibility(View.VISIBLE);
        btnErrorRefresh.setOnClickListener(v -> {
            loadIngredients();
        });
    }

}