package com.example.laptopmart.laptop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityAddLaptopBinding;

public class AddLaptopActivity extends AppCompatActivity {

    private ActivityAddLaptopBinding binding;
    private AddLaptopViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLaptopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AddLaptopViewModel.class);

        initButton();
        observeViewModel();
    }

    private void initButton() {
        binding.btnSave.setOnClickListener(v -> addLaptop());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, this::setLoadingState);
        viewModel.getToastMessage().observe(this, message -> {
            if ("Berhasil menambahkan laptop!".equals(message)) {
                showToast(message);
                finish();
            } else if (message != null) {
                showToast(message);
            }
        });
    }

    private void addLaptop() {
        String name = binding.etName.getText().toString().trim();
        String brand = binding.etBrand.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String price = binding.etPrice.getText().toString().trim();
        String stock = binding.etStock.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();

        if (isValidInput(name, brand, description, price, stock, imageUrl)) {
            viewModel.saveLaptop(name, brand, description, price, stock, imageUrl);
        }
    }

    private boolean isValidInput(String name, String brand, String description, String price, String stock, String imageUrl) {
        boolean isValid = true;
        if (name.isEmpty()) {
            binding.tilName.setError("Nama laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilName.setError(null);
        }
        if (brand.isEmpty()) {
            binding.tilBrand.setError("Brand laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilBrand.setError(null);
        }
        if (description.isEmpty()) {
            binding.tilDescription.setError("Deskripsi laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilDescription.setError(null);
        }
        if (price.isEmpty()) {
            binding.tilPrice.setError("Harga laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilPrice.setError(null);
        }
        if (stock.isEmpty()) {
            binding.tilStock.setError("Stok laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilStock.setError(null);
        }
        if (imageUrl.isEmpty()) {
            binding.tilImageUrl.setError("Gambar laptop tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilImageUrl.setError(null);
        }
        return isValid;
    }

    private void setLoadingState(boolean isLoading) {
        binding.etName.setEnabled(!isLoading);
        binding.etBrand.setEnabled(!isLoading);
        binding.etDescription.setEnabled(!isLoading);
        binding.etPrice.setEnabled(!isLoading);
        binding.etStock.setEnabled(!isLoading);
        binding.btnSave.setEnabled(!isLoading);

        if (isLoading) {
            binding.btnSave.setText(R.string.process);
        } else {
            binding.btnSave.setText(R.string.save);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}