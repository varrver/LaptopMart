package com.example.laptopmart.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laptopmart.databinding.ViewHolderCartBinding;
import com.example.laptopmart.model.CartItem;

import java.text.NumberFormat;
import java.util.Locale;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {
    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getLaptopName().equals(newItem.getLaptopName()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    oldItem.getQuantity() == newItem.getQuantity() &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl());
        }
    };
    private final OnCartClickListener listener;

    public CartAdapter(OnCartClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderCartBinding binding = ViewHolderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem currentCartItem = getItem(position);
        holder.bind(currentCartItem);
    }

    public interface OnCartClickListener {
        void onCartClick(CartItem cartItem);

        void onPlusCLick(CartItem cartItem);

        void onMinusClick(CartItem cartItem);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCartBinding binding;

        public CartViewHolder(ViewHolderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCartClick(getItem(position));
                }
            });

            binding.btnPlus.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPlusCLick(getItem(position));
                }
            });

            binding.btnMinus.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onMinusClick(getItem(position));
                }
            });
        }

        public void bind(CartItem cartItem) {
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String name = cartItem.getLaptopName();
            String formattedPrice = formatRupiah.format(cartItem.getPrice());
            String quantity = String.valueOf(cartItem.getQuantity());

            binding.tvCartName.setText(name);
            binding.tvCartPrice.setText(formattedPrice);
            binding.tvCartQuantity.setText(quantity);

            Glide.with(binding.getRoot())
                    .load(cartItem.getImageUrl())
                    .into(binding.ivCartImage);
        }
    }
}
