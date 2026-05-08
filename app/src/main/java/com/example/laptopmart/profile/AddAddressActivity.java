package com.example.laptopmart.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.ActivityAddAddressBinding;
import com.example.laptopmart.model.UserAddress;

public class AddAddressActivity extends AppCompatActivity {

    private ActivityAddAddressBinding binding;
    private AddressViewModel viewModel;
    private UserAddress existingAddress;

    private final ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedAddress = result.getData().getStringExtra("SELECTED_ADDRESS");
                    binding.etFullAddress.setText(selectedAddress);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        existingAddress = (UserAddress) getIntent().getSerializableExtra("EXTRA_ADDRESS");

        if (existingAddress != null) {
            binding.tvHeader.setText("Edit Alamat");
            fillFields();
        }

        initButtons();
        observeViewModel();
    }

    private void fillFields() {
        binding.etLabel.setText(existingAddress.getLabel());
        binding.etReceiverName.setText(existingAddress.getReceiverName());
        binding.etReceiverPhone.setText(existingAddress.getReceiverPhone());
        binding.etFullAddress.setText(existingAddress.getFullAddress());
        binding.switchDefault.setChecked(existingAddress.isDefault());
    }

    private void initButtons() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapPickerActivity.class);
            mapLauncher.launch(intent);
        });
        binding.btnSaveAddress.setOnClickListener(v -> saveAddress());
    }

    private void saveAddress() {
        String label = binding.etLabel.getText().toString().trim();
        String name = binding.etReceiverName.getText().toString().trim();
        String phone = binding.etReceiverPhone.getText().toString().trim();
        String address = binding.etFullAddress.getText().toString().trim();
        boolean isDefault = binding.switchDefault.isChecked();

        if (label.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserAddress newAddress = new UserAddress(
                existingAddress != null ? existingAddress.getId() : null,
                label, name, phone, address, isDefault
        );

        if (existingAddress != null) {
            viewModel.updateAddress(newAddress);
        } else {
            viewModel.addAddress(newAddress);
        }
    }

    private void observeViewModel() {
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("berhasil")) {
                    finish();
                }
            }
        });
    }
}
