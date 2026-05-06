package com.example.laptopmart.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laptopmart.databinding.ActivityProfileDetailBinding;

public class ProfileDetailActivity extends AppCompatActivity {
    private ActivityProfileDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}