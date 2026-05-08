package com.example.laptopmart.laptop;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityAddEditLaptopBinding;

public class AddEditLaptopActivity extends AppCompatActivity {

    private ActivityAddEditLaptopBinding binding;
    private AddEditLaptopViewModel viewModel;
    private Uri selectedImageUri = null;
    private boolean isEditMode = false;
    private String laptopId = "";
    private String existingImageUrl = "";

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this)
                            .load(uri)
                            .into(binding.ivLaptopImage);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditLaptopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AddEditLaptopViewModel.class);

        initButton();
        observeViewModel();
        checkEditMode();
    }

    private void initButton() {
        binding.btnPickImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        binding.btnSave.setOnClickListener(v -> addLaptop());
        binding.btnDelete.setOnClickListener(v -> {
            viewModel.deleteLaptop(laptopId);
            finish();
        });
        binding.ivBack.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, this::setLoadingState);
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                showToast(message);
                if (message.contains("Berhasil")) {
                    finish();
                }
            }
        });
    }

    private void addLaptop() {
        String name = binding.etName.getText().toString().trim();
        String brand = binding.etBrand.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String price = binding.etPrice.getText().toString().trim();
        String stock = binding.etStock.getText().toString().trim();

        if (isValidInput(name, brand, description, price, stock, selectedImageUri)) {
            viewModel.saveLaptop(laptopId, name, brand, description, price, stock, selectedImageUri, existingImageUrl);
        }
    }

    private void checkEditMode() {
        isEditMode = getIntent().getBooleanExtra("IS_EDIT_MODE", false);

        if (isEditMode) {
            laptopId = getIntent().getStringExtra("LAPTOP_ID");
            existingImageUrl = getIntent().getStringExtra("LAPTOP_IMAGE");

            String name = getIntent().getStringExtra("LAPTOP_NAME");
            String brand = getIntent().getStringExtra("LAPTOP_BRAND");
            String description = getIntent().getStringExtra("LAPTOP_DESCRIPTION");
            String price = getIntent().getStringExtra("LAPTOP_PRICE");
            String stock = getIntent().getStringExtra("LAPTOP_STOCK");

            binding.etName.setText(name);
            binding.etBrand.setText(brand);
            binding.etDescription.setText(description);
            binding.etPrice.setText(price);
            binding.etStock.setText(stock);

            Glide.with(this).load(existingImageUrl).into(binding.ivLaptopImage);

            binding.btnSave.setText(R.string.update);

            binding.btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidInput(String name, String brand, String description, String price, String stock, Uri selectedImageUri) {
        boolean isValid = true;
        if (selectedImageUri == null && (existingImageUrl == null || existingImageUrl.isEmpty())) {
            showToast("Silahkan pilih gambar terlebih dahulu!");
            isValid = false;
        }
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
            if (isEditMode) {
                binding.btnSave.setText(R.string.update);
            } else {
                binding.btnSave.setText(R.string.save);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}