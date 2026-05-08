package com.example.laptopmart.profile;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.ActivityProfileDetailBinding;

public class ProfileDetailActivity extends AppCompatActivity {
    private ActivityProfileDetailBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        initButton();
        observeModel();
    }

    private void observeModel() {
        profileViewModel.getUserProfileLiveData().observe(this, profile -> {
            if (profile != null) {
                binding.etDetailName.setText(profile.getName());
                binding.etDetailPhone.setText(profile.getPhone());
            }
        });
        profileViewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("berhasil")) {
                    finish(); // Go back to the main Profile screen!
                }
            }
        });
    }

    private void initButton() {
        binding.btnSaveProfile.setOnClickListener(v -> {
            String name = binding.etDetailName.getText().toString().trim();
            String phone = binding.etDetailPhone.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Nama dan nomor telepon harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tell the ViewModel to save it!
            profileViewModel.updateFullProfile(name, phone, "");
        });
        binding.ivBack.setOnClickListener(v -> finish());
    }
}
