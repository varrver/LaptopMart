package com.example.laptopmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;
import com.example.laptopmart.domain.BannerModel;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<BannerModel> bannerItems;

    public BannerAdapter(List<BannerModel> bannerItems) {
        this.bannerItems = bannerItems;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlide;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlide = itemView.findViewById(R.id.ivSlide);
        }
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        String url = bannerItems.get(position).getUrl();

        Glide.with(holder.itemView.getContext())
                .load(url)
                .into(holder.ivSlide);
    }

    @Override
    public int getItemCount() {
        return bannerItems != null ? bannerItems.size() : 0;
    }
}