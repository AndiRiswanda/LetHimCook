package com.example.lethimcook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Model.Ingredient;
import com.example.lethimcook.Model.IngredientMeasure;
import com.example.lethimcook.R;
import com.example.lethimcook.SpiceDetailActivity;

import java.util.List;

public class IngredientMeasureAdapter extends RecyclerView.Adapter<IngredientMeasureAdapter.IngredientViewHolder> {

    private List<IngredientMeasure> ingredients;

    public interface OnIngredientClickListener {
        void onIngredientClick(IngredientMeasure ingredient);
    }

    private OnIngredientClickListener listener;

    public IngredientMeasureAdapter(List<IngredientMeasure> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientMeasureAdapter(List<IngredientMeasure> ingredients, OnIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    public void setOnIngredientClickListener(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void updateIngredients(List<IngredientMeasure> newIngredients) {
        this.ingredients = newIngredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_measure, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientMeasure item = ingredients.get(position);
        holder.tvIngredient.setText(item.getIngredient());
        holder.tvMeasure.setText(item.getMeasure());

        // Load ingredient image
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.placeholder_ingredient)
                .into(holder.imgIngredient);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredient;
        TextView tvMeasure;
        ImageView imgIngredient;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tvIngredient);
            tvMeasure = itemView.findViewById(R.id.tvMeasure);
            imgIngredient = itemView.findViewById(R.id.imgMealThumb);
        }
    }
}