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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    private List<Meal> mealList;
    private OnMealClickListener listener;

    public MealAdapter(List<Meal> mealList, OnMealClickListener listener) {
        this.mealList = mealList;
        this.listener = listener;
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

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName;
        MaterialButton btnDetail;  // Changed from ImageButton to MaterialButton

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgMealThumb);
            tvName = itemView.findViewById(R.id.tvMealName);
            btnDetail = itemView.findViewById(R.id.btnMealDetail);  // This should now match the XML
        }
    }
}