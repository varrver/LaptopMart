package com.example.laptopmart.laptop;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laptopmart.databinding.ViewholderLaptopBinding;
import com.example.laptopmart.model.Laptop;

import java.text.NumberFormat;
import java.util.Locale;

public class LaptopAdapter extends ListAdapter<Laptop, LaptopAdapter.LaptopViewHolder> {

    private static final DiffUtil.ItemCallback<Laptop> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Laptop oldItem, @NonNull Laptop newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Laptop oldItem, @NonNull Laptop newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    oldItem.getStock() == newItem.getStock() &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl());
        }
    };

    public interface OnLaptopClickListener {
        void onLaptopClick(Laptop laptop);
    }

    private final OnLaptopClickListener listener;

    public LaptopAdapter(OnLaptopClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public LaptopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderLaptopBinding binding = ViewholderLaptopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LaptopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LaptopAdapter.LaptopViewHolder holder, int position) {
        Laptop currentLaptop = getItem(position);
        holder.bind(currentLaptop);
    }

    class LaptopViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderLaptopBinding binding;

        public LaptopViewHolder(ViewholderLaptopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onLaptopClick(getItem(position));
                }
            });
        }

        public void bind(Laptop laptop) {
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String name = laptop.getName();
            String formattedPrice = formatRupiah.format(laptop.getPrice());
            String stock = String.valueOf(laptop.getStock());

            binding.tvLaptopName.setText(name);
            binding.tvLaptopPrice.setText(formattedPrice);
            binding.tvLaptopStock.setText("Stok: " + stock);

            Glide.with(binding.getRoot())
                    .load(laptop.getImageUrl())
                    .into(binding.ivLaptopImage);
        }
    }
}
