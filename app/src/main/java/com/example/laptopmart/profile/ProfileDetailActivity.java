package com.example.laptopmart.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private final ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedAddress = result.getData().getStringExtra("SELECTED_ADDRESS");
                    // Magically put the address into the EditText!
                    binding.etDetailAddress.setText(selectedAddress);
                }
            }
    );

    private void observeModel() {
        profileViewModel.getUserProfileLiveData().observe(this, profile -> {
            if (profile != null) {
                binding.etDetailName.setText(profile.getName());
                binding.etDetailPhone.setText(profile.getPhone());
                if (profile.getAddress() != null) {
                    binding.etDetailAddress.setText(profile.getAddress());
                }
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

    // 2. Inside your initButton() method, tell the Map button to launch it:
    private void initButton() {
        binding.btnOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapPickerActivity.class);
            mapLauncher.launch(intent);
        });
        binding.btnSaveProfile.setOnClickListener(v -> {
            String name = binding.etDetailName.getText().toString().trim();
            String phone = binding.etDetailPhone.getText().toString().trim();
            String address = binding.etDetailAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tell the ViewModel to save it!
            profileViewModel.updateFullProfile(name, phone, address);
        });
    }
}