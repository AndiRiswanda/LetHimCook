package com.example.lethimcook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lethimcook.R;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.db.FavoritesDbHelper;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void onFavoriteToggle(Meal meal, boolean isFavorite);
    }

    private List<Meal> mealList;
    private OnMealClickListener listener;
    private FavoritesDbHelper dbHelper;

    public MealAdapter(List<Meal> mealList, OnMealClickListener listener, FavoritesDbHelper dbHelper) {
        this.mealList = mealList;
        this.listener = listener;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.tvName.setText(meal.getName());

        // Load thumbnail via Glide
        Glide.with(holder.itemView.getContext())
                .load(meal.getThumbnailUrl())
                .placeholder(R.drawable.placeholder_ingredient)
                .into(holder.imgThumb);

        // Check if meal is favorite
        boolean isFavorite = dbHelper.isFavorite(meal.getIdMeal());
        updateFavoriteButtonState(holder.btnFavorite, isFavorite);

        // Set click listeners
        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });

        holder.btnFavorite.setOnClickListener(v -> {
            if (listener != null) {
                boolean newState = !dbHelper.isFavorite(meal.getIdMeal());
                listener.onFavoriteToggle(meal, newState);
                updateFavoriteButtonState(holder.btnFavorite, newState);
            }
        });
    }

    private void updateFavoriteButtonState(MaterialButton button, boolean isFavorite) {
        if (isFavorite) {
            button.setIconTint(button.getContext().getColorStateList(R.color.favorite_active));
            button.setIcon(button.getContext().getDrawable(R.drawable.baseline_favorite_24));
        } else {
            button.setIconTint(button.getContext().getColorStateList(R.color.favorite_inactive));
            button.setIcon(button.getContext().getDrawable(R.drawable.baseline_favorite_border_24));
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName;
        MaterialButton btnDetail, btnFavorite;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgMealThumb);
            tvName = itemView.findViewById(R.id.tvMealName);
            btnDetail = itemView.findViewById(R.id.btnMealDetail);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }

    public void refreshFavoriteStatus() {
        notifyDataSetChanged();
    }
}