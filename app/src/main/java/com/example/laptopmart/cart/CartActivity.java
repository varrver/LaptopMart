package com.example.laptopmart.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.ActivityCartBinding;
import com.example.laptopmart.laptop.DetailLaptopActivity;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.order.CheckoutActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupRecyclerView();
        observeModel();
        initButton();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(CartItem cartItem) {
                Intent intent = new Intent(CartActivity.this, DetailLaptopActivity.class);
                // ALL WE NEED TO PASS IS THE ID! The Activity will do the rest!
                intent.putExtra("LAPTOP_ID", cartItem.getLaptopId());
                startActivity(intent);
            }

            @Override
            public void onPlusCLick(CartItem cartItem) {
                viewModel.increaseQuantity(cartItem);
            }

            @Override
            public void onMinusClick(CartItem cartItem) {
                viewModel.decreaseQuantity(cartItem);
            }
        });
        binding.rvCart.setAdapter(adapter);
    }

    private void observeModel() {
        viewModel.getCartItemsLiveData().observe(this, cartItems -> {
            if (cartItems != null) {
                adapter.submitList(cartItems);

                boolean isEmpty = cartItems.isEmpty();
                binding.layoutEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                binding.rvCart.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.bottomCheckoutBar.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });
        viewModel.getTotalPriceLiveData().observe(this, totalPrice -> {
            if (totalPrice != null) {
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String formattedPrice = formatRupiah.format(totalPrice);
                binding.tvTotalPrice.setText(formattedPrice);
            }
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                showToast(error);
            }
        });
    }

    private void initButton() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnStartShopping.setOnClickListener(v -> finish());

        binding.btnCheckout.setOnClickListener(v -> {
            List<CartItem> items = viewModel.getCartItemsLiveData().getValue();
            Double totalPrice = viewModel.getTotalPriceLiveData().getValue();
            if (items == null || items.isEmpty()) {
                showToast("Keranjang Anda masih kosong!");
                return;
            }
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("CART_ITEMS", new java.util.ArrayList<>(items));
            intent.putExtra("TOTAL_PRICE", totalPrice != null ? totalPrice : 0.0);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}