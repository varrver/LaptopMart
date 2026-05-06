package com.example.laptopmart.laptop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityDetailLaptopBinding;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.order.CheckoutActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailLaptopActivity extends AppCompatActivity {

    private ActivityDetailLaptopBinding binding;
    private DetailLaptopViewModel viewModel;
    private String laptopId = "";
    private String laptopName;
    private String laptopImageUrl;
    private double laptopPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailLaptopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DetailLaptopViewModel.class);

        observeViewModel();
        initLaptop();
        initButton();
    }

    private void observeViewModel() {
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                showToast(message);
                if (message.contains("Berhasil") || message.contains("ditambah")) {
                    finish();
                }
            }
        });
        viewModel.getIsLoading().observe(this, this::setLoadingState);
        viewModel.getLaptopLiveData().observe(this, laptop -> {
            if (laptop != null) {
                // Save these to the class variables so the "Add to Cart" button still works!
                laptopName = laptop.getName();
                laptopImageUrl = laptop.getImageUrl();
                laptopPrice = laptop.getPrice();

                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String formattedPrice = formatRupiah.format(laptopPrice);

                binding.tvLaptopName.setText(laptopName);
                binding.tvLaptopBrand.setText(laptop.getBrand());
                binding.tvLaptopDescription.setText(laptop.getDescription());
                binding.tvLaptopPrice.setText(formattedPrice);
                binding.tvLaptopStock.setText("Stok: " + laptop.getStock());

                Glide.with(this).load(laptopImageUrl).into(binding.ivLaptopImage);
            }
        });
    }

    private void initLaptop() {
        laptopId = getIntent().getStringExtra("LAPTOP_ID");
        viewModel.loadLaptopDetails(laptopId);
    }

    private void initButton() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnCart.setOnClickListener(v -> {
            if (laptopName != null) {
                viewModel.saveCart("", laptopId, laptopName, laptopImageUrl, laptopPrice, 0);
            }
        });
        binding.btnBuy.setOnClickListener(v -> {
            // Prevent clicking if the data hasn't finished loading yet!
            if (laptopName == null) {
                showToast("Data sedang dimuat, tunggu sebentar...");
                return;
            }

            // 1. Create a "Direct Buy" CartItem (Quantity = 1)
            // We give it a dummy ID "temp_buy_now" because it isn't actually saved in the cart database!
            CartItem directBuyItem = new CartItem(
                    "temp_buy_now",
                    laptopId,
                    laptopName,
                    laptopImageUrl,
                    laptopPrice,
                    1
            );

            // 2. Put it inside an ArrayList
            java.util.ArrayList<CartItem> checkoutList = new java.util.ArrayList<>();
            checkoutList.add(directBuyItem);

            // 3. Send it directly to the Checkout screen!
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("CART_ITEMS", checkoutList);
            intent.putExtra("TOTAL_PRICE", laptopPrice); // Total price is just the price of this 1 laptop
            startActivity(intent);
        });
    }

    private void setLoadingState(boolean isLoading) {
        binding.btnCart.setEnabled(!isLoading);
        binding.btnBuy.setEnabled(!isLoading);

        if (isLoading) {
            binding.btnCart.setText(R.string.process);
            binding.btnBuy.setText(R.string.process);
        } else {
            binding.btnCart.setText(R.string.cart);
            binding.btnBuy.setText(R.string.buy);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}