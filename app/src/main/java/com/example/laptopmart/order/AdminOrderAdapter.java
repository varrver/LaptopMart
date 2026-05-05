package com.example.laptopmart.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laptopmart.databinding.ViewHolderOrderBinding;
import com.example.laptopmart.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
            binding.tvCustomerName.setText(order.getCustomerName());
            binding.tvCustomerAddress.setText(order.getAddress());
            binding.tvOrderStatus.setText("Status: " + order.getStatus());
            binding.tvOrderTotal.setText(formattedTotal);
            binding.tvOrderDate.setText(formattedDate);
        }
    }
}