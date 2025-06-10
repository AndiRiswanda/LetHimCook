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
import com.example.lethimcook.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    private List<Ingredient> ingredientList;
    private OnIngredientClickListener listener;

    public IngredientAdapter(List<Ingredient> ingredientList, OnIngredientClickListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ing = ingredientList.get(position);
        holder.tvName.setText(ing.getName());

        // Load image from URL if available, otherwise use resource
        if (ing.hasImageUrl()) {
            Glide.with(holder.itemView.getContext())
                    .load(ing.getImageUrl())
                    .placeholder(R.drawable.placeholder_ingredient)
                    .into(holder.img);
        } else {
            holder.img.setImageResource(ing.getImageResId());
        }

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientClick(ing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName;
        MaterialButton btnDetail;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgIngredient);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}