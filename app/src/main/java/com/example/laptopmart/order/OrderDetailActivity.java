package com.example.laptopmart.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.laptopmart.R;
import com.example.laptopmart.cart.CartAdapter;
import com.example.laptopmart.databinding.ActivityOrderDetailBinding;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Order order = (Order) getIntent().getSerializableExtra("ORDER_DATA");
        if (order != null) {
            displayOrderDetails(order);
        }

        binding.ivBack.setOnClickListener(v -> finish());
    }

    private void displayOrderDetails(Order order) {
        // Format Currency
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedTotal = formatRupiah.format(order.getTotalPrice());

        // Format Date
        Date date = new Date(order.getOrderDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"));
        String formattedDate = dateFormat.format(date);

        // Bind Data
        binding.tvDetailStatus.setText(order.getStatus());
        binding.tvDetailDate.setText(formattedDate);
        binding.tvDetailName.setText("Nama: " + order.getCustomerName());
        binding.tvDetailAddress.setText("Alamat: " + order.getAddress());
        binding.tvDetailShipping.setText("Metode Pengiriman: " + (order.getShippingMethod() != null ? order.getShippingMethod() : "-"));
        binding.tvDetailPayment.setText("Metode Pembayaran: " + (order.getPaymentMethod() != null ? order.getPaymentMethod() : "-"));
        binding.tvDetailTotal.setText(formattedTotal);

        setStatusStyle(order.getStatus());

        // Setup RecyclerView for items
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(CartItem cartItem) {
                // No action needed in detail view
            }

            @Override
            public void onPlusCLick(CartItem cartItem) {
                // Read-only view
            }

            @Override
            public void onMinusClick(CartItem cartItem) {
                // Read-only view
            }
        });

        binding.rvDetailItems.setAdapter(adapter);
        binding.rvDetailItems.setNestedScrollingEnabled(false);
        adapter.submitList(order.getItems());
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
                    backgroundRes = R.drawable.bg_status_pending;
                    textColorRes = R.color.info_blue;
                    break;
            }
        }

        binding.tvDetailStatus.setBackgroundResource(backgroundRes);
        binding.tvDetailStatus.setTextColor(ContextCompat.getColor(this, textColorRes));
    }
}
