package com.example.laptopmart.order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ViewHolderOrderBinding;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;

public class AdminOrderAdapter extends ListAdapter<Order, AdminOrderAdapter.OrderViewHolder> {

    // 1. The DiffUtil Calculator
    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getOrderId().equals(newItem.getOrderId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getStatus().equals(newItem.getStatus()); // Usually, only the status changes!
        }
    };

    // 2. The Click Listener Interface
    public interface OnOrderClickListener {
        void onUpdateStatusClick(Order order);
    }

    private final OnOrderClickListener listener;
    private final boolean isAdmin;

    public AdminOrderAdapter(boolean isAdmin, OnOrderClickListener listener) {
        super(DIFF_CALLBACK);
        this.isAdmin = isAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderOrderBinding binding = ViewHolderOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // 3. The ViewHolder
    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderOrderBinding binding;

        public OrderViewHolder(ViewHolderOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            if (!isAdmin) {
                binding.btnUpdateStatus.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = getItem(position);
                    Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
                    intent.putExtra("ORDER_DATA", order);
                    v.getContext().startActivity(intent);
                }
            });

            binding.btnUpdateStatus.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onUpdateStatusClick(getItem(position));
                }
            });
        }

        public void bind(Order order) {
            // Format the Money
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedTotal = formatRupiah.format(order.getTotalPrice());

            // Format the Date
            Date date = new Date(order.getOrderDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"));
            String formattedDate = dateFormat.format(date);

            // Set the texts
            binding.tvOrderStatus.setText(order.getStatus());
            binding.tvOrderTotal.setText(formattedTotal);
            binding.tvOrderDate.setText(formattedDate);

            // Set Product Info
            List<CartItem> items = order.getItems();
            if (items != null && !items.isEmpty()) {
                CartItem firstItem = items.get(0);
                binding.tvOrderProductName.setText(firstItem.getLaptopName());

                Glide.with(itemView.getContext())
                        .load(firstItem.getImageUrl())
                        .placeholder(R.drawable.ic_image)
                        .into(binding.ivOrderProduct);

                if (items.size() > 1) {
                    binding.tvOtherItems.setVisibility(View.VISIBLE);
                    binding.tvOtherItems.setText("+" + (items.size() - 1) + " item lainnya");
                } else {
                    binding.tvOtherItems.setVisibility(View.GONE);
                }
            }

            // Set Status Style
            setStatusStyle(order.getStatus());
        }

        private void setStatusStyle(String status) {
            int backgroundRes = R.drawable.bg_status_pending;
            int textColorRes = R.color.navy_blue;

            if (status != null) {
                switch (status) {
                    case "Selesai":
                        backgroundRes = R.drawable.bg_status_success;
                        textColorRes = R.color.success_green;
                        break;
                    case "Batal":
                        backgroundRes = R.drawable.bg_status_error;
                        textColorRes = R.color.christmas_red;
                        break;
                    case "Proses":
                        backgroundRes = R.drawable.bg_status_pending; // Or a specific blue one
                        textColorRes = R.color.info_blue;
                        break;
                }
            }

            binding.tvOrderStatus.setBackgroundResource(backgroundRes);
            binding.tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), textColorRes));
        }
    }
}