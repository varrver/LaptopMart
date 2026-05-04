package com.example.laptopmart.laptop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.laptopmart.databinding.ActivityDetailLaptopBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailLaptopActivity extends AppCompatActivity {

    private ActivityDetailLaptopBinding binding;
    private String laptopId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailLaptopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initButton();
        initLaptop();
    }

    private void initButton() {
        binding.ivBack.setOnClickListener(v -> finish());
    }

    private void initLaptop() {
        laptopId = getIntent().getStringExtra("LAPTOP_ID");
        String name = getIntent().getStringExtra("LAPTOP_NAME");
        String brand = getIntent().getStringExtra("LAPTOP_BRAND");
        String description = getIntent().getStringExtra("LAPTOP_DESCRIPTION");
        double price = getIntent().getDoubleExtra("LAPTOP_PRICE", 0.0);
        int stock = getIntent().getIntExtra("LAPTOP_STOCK", 0);
        String image = getIntent().getStringExtra("LAPTOP_IMAGE");

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedPrice = formatRupiah.format(price);

        binding.tvLaptopName.setText(name);
        binding.tvLaptopBrand.setText(brand);
        binding.tvLaptopDescription.setText(description);
        binding.tvLaptopPrice.setText(formattedPrice);
        binding.tvLaptopStock.setText("Stok: " + stock);

        Glide.with(this).load(image).into(binding.ivLaptopImage);
    }
}