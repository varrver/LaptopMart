package com.example.laptopmart.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<String> bannerUrls;

    public BannerAdapter(List<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We inflate our simple item_banner.xml here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // Load the image using Glide!
        Glide.with(holder.itemView.getContext())
                .load(bannerUrls.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bannerUrls.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Since we didn't use ViewBinding for this 1-line layout, standard findViewById is fine!
            imageView = itemView.findViewById(R.id.iv_banner_image);
        }
    }
}