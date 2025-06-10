package com.example.lethimcook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lethimcook.R;
import com.google.android.material.button.MaterialButton;

import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    private CardView cardSpice, cardRecipe;
    private TextView tvTip;
    private String[] broTips = {
            "Turmeric is bright yellow, while ginger is pale brown with a fibrous texture.",
            "Salt isn't just for flavor - it helps draw out moisture when cooking meat.",
            "Always wash your hands after handling chili peppers!",
            "When a recipe says 'simmer', that means low heat with just a few bubbles.",
            "A sharp knife is safer than a dull one - it requires less force to use.",
            "Use a meat thermometer to ensure your meat is cooked perfectly.",
            "Let cooked meat rest for a few minutes before slicing to retain juices.",
            "Store spices in a cool, dark place to keep them fresh longer.",
            "Add fresh herbs at the end of cooking for the best flavor.",
            "Use parchment paper to prevent baked goods from sticking.",
            "Don't overcrowd the pan when sautéing - it lowers the temperature.",
            "Taste as you cook to adjust seasoning as needed.",
            "Use a splash of vinegar to brighten up soups and stews.",
            "Keep your knives sharp for easier and safer cutting.",
            "Use a damp towel under your cutting board to keep it from slipping.",
            "Preheat your pans before adding ingredients for better cooking.",
            "Always read the recipe fully before starting to cook.",
            "Use a scale for baking to ensure accurate measurements.",
            "Let bread dough rise in a warm, draft-free place.",
            "Clean as you go to keep your workspace organized."
    };
    private int currentTipIndex = 0;
    private Handler handler = new Handler();
    private Runnable tipUpdater;

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardSpice = view.findViewById(R.id.cardSpice);
        cardRecipe = view.findViewById(R.id.cardRecipe);
        tvTip = view.findViewById(R.id.tvTipContent);

        // Set initial tip
        tvTip.setText(broTips[currentTipIndex]);

        // Start updating tips every 15 seconds
        tipUpdater = new Runnable() {
            @Override
            public void run() {
                currentTipIndex = (currentTipIndex + 1) % broTips.length; // Cycle through tips
                tvTip.setText(broTips[currentTipIndex]);
                handler.postDelayed(this, 15000); // Schedule next update in 15 seconds
            }
        };
        handler.postDelayed(tipUpdater, 15000);

        cardSpice.setOnClickListener(v -> {
            // Launch SpiceIdentifierActivity
            Intent intent = new Intent(getActivity(), SpiceIdentifierActivity.class);
            startActivity(intent);
        });

        cardRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CommonMistakesActivity.class);
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(tipUpdater); // Stop updates when the view is destroyed
    }
}