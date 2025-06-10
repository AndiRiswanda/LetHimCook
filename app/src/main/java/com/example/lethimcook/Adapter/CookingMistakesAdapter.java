package com.example.lethimcook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lethimcook.Model.CookingMistake;
import com.example.lethimcook.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CookingMistakesAdapter extends RecyclerView.Adapter<CookingMistakesAdapter.MistakeViewHolder> {

    public interface OnMistakeClickListener {
        void onMistakeClick(CookingMistake mistake);
    }

    private List<CookingMistake> mistakeList;
    private OnMistakeClickListener listener;

    public CookingMistakesAdapter(List<CookingMistake> mistakeList, OnMistakeClickListener listener) {
        this.mistakeList = mistakeList;
        this.listener = listener;
    }

    public void updateMistakes(List<CookingMistake> newMistakes) {
        this.mistakeList = newMistakes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MistakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cooking_mistake, parent, false);
        return new MistakeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MistakeViewHolder holder, int position) {
        CookingMistake mistake = mistakeList.get(position);
        holder.tvTitle.setText(mistake.getName());
        holder.tvDescription.setText(mistake.getDescription());
        holder.tvCategory.setText(mistake.getCategory());

        // Load image
        if (mistake.hasImageUrl()) {
            Glide.with(holder.itemView.getContext())
                    .load(mistake.getImageUrl())
                    .placeholder(R.drawable.placeholder_ingredient)
                    .into(holder.imgMistake);
        } else if (mistake.getImageResId() != 0) {
            holder.imgMistake.setImageResource(mistake.getImageResId());
        } else {
            holder.imgMistake.setImageResource(R.drawable.placeholder_ingredient);
        }

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMistakeClick(mistake);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mistakeList.size();
    }

    static class MistakeViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView imgMistake;
        TextView tvTitle, tvDescription, tvCategory;

        public MistakeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardMistake);
            imgMistake = itemView.findViewById(R.id.imgMistake);
            tvTitle = itemView.findViewById(R.id.tvMistakeTitle);
            tvDescription = itemView.findViewById(R.id.tvMistakeDescription);
            tvCategory = itemView.findViewById(R.id.tvMistakeCategory);
        }
    }
}