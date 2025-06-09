package com.example.lethimcook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Model.Meal;
import com.example.lethimcook.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteViewHolder> {

    public interface OnFavoriteMealClickListener {
        void onFavoriteMealClick(Meal meal);
        void onRemoveFavorite(Meal meal);
    }

    private List<Meal> mealList;
    private OnFavoriteMealClickListener listener;

    public FavoriteMealsAdapter(List<Meal> mealList, OnFavoriteMealClickListener listener) {
        this.mealList = mealList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_meal, parent, false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.tvName.setText(meal.getName());

        // Show category and area if available
        String subtitle = "";
        if (meal.getCategory() != null && !meal.getCategory().isEmpty()) {
            subtitle += meal.getCategory();
        }
        if (meal.getArea() != null && !meal.getArea().isEmpty()) {
            if (!subtitle.isEmpty()) subtitle += " • ";
            subtitle += meal.getArea();
        }
        holder.tvSubtitle.setText(subtitle);

        // Load thumbnail via Glide
        Glide.with(holder.itemView.getContext())
                .load(meal.getThumbnailUrl())
                .placeholder(R.drawable.placeholder_ingredient)
                .into(holder.imgThumb);

        // Set click listeners
        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteMealClick(meal);
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveFavorite(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName, tvSubtitle;
        MaterialButton btnDetail, btnRemove;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgFavoriteMealThumb);
            tvName = itemView.findViewById(R.id.tvFavoriteMealName);
            tvSubtitle = itemView.findViewById(R.id.tvFavoriteMealSubtitle);
            btnDetail = itemView.findViewById(R.id.btnFavoriteMealDetail);
            btnRemove = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
}