package com.example.laptopmart.laptop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityDetailLaptopBinding;

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
        initButton();
        initLaptop();
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
    }

    private void initButton() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnCart.setOnClickListener(v -> viewModel.saveCart("", laptopId, laptopName, laptopImageUrl, laptopPrice, 0));
    }

    private void initLaptop() {
        laptopId = getIntent().getStringExtra("LAPTOP_ID");
        laptopName = getIntent().getStringExtra("LAPTOP_NAME");
        String brand = getIntent().getStringExtra("LAPTOP_BRAND");
        String description = getIntent().getStringExtra("LAPTOP_DESCRIPTION");
        laptopPrice = getIntent().getDoubleExtra("LAPTOP_PRICE", 0.0);
        int stock = getIntent().getIntExtra("LAPTOP_STOCK", 0);
        laptopImageUrl = getIntent().getStringExtra("LAPTOP_IMAGE");

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedPrice = formatRupiah.format(laptopPrice);

        binding.tvLaptopName.setText(laptopName);
        binding.tvLaptopBrand.setText(brand);
        binding.tvLaptopDescription.setText(description);
        binding.tvLaptopPrice.setText(formattedPrice);
        binding.tvLaptopStock.setText("Stok: " + stock);

        Glide.with(this).load(laptopImageUrl).into(binding.ivLaptopImage);
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