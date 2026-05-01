package com.example.laptopmart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.admin.DashboardActivity;
import com.example.laptopmart.databinding.ActivityRegisterBinding;
import com.example.laptopmart.home.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s]).+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initButton();
        observeViewModel();
    }

    private void initButton() {
        binding.btnRegister.setOnClickListener(v -> registerAccount());
        binding.btnLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, this::setLoadingState);
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null) {
                showToast(errorMsg);
            }
        });
        viewModel.getNavigateTo().observe(this, destination -> {
            if ("ADMIN".equals(destination)) {
                navigateToDashboard();
            } else if ("MAIN".equals(destination)) {
                navigateToMain();
            }
        });
    }

    private void registerAccount() {
        String name = binding.etName.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (isValidInput(name, phone, email, password)) {
            viewModel.register(name, phone, email, password);
        }
    }

    private boolean isValidInput(String name, String phone, String email, String password) {
        boolean isValid = true;
        if (name.isEmpty()) {
            binding.tilName.setError("Nama tidak boleh kosong");
            isValid = false;
        } else {
            binding.tilName.setError(null);
        }
        if (phone.isEmpty()) {
            binding.tilPhone.setError("Nomor telepon tidak boleh kosong");
            isValid = false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.tilPhone.setError("Format nomor telepon tidak valid!");
            isValid = false;
        } else {
            binding.tilPhone.setError(null);
        }
        if (email.isEmpty()) {
            binding.tilEmail.setError("Email tidak boleh kosong");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Format email tidak valid!");
            isValid = false;
        } else {
            binding.tilEmail.setError(null);
        }
        if (password.isEmpty()) {
            binding.tilPassword.setError("Password tidak boleh kosong");
            isValid = false;
        } else if (password.length() < 8) {
            binding.tilPassword.setError("Password minimal 8 karakter!");
            isValid = false;
        } else if (!password.matches(PASSWORD_PATTERN)) {
            binding.tilPassword.setError("Password harus berisi huruf, angka, dan karakter khusus!");
            isValid = false;
        } else {
            binding.tilPassword.setError(null);
        }
        return isValid;
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoadingState(boolean isLoading) {
        binding.etName.setEnabled(!isLoading);
        binding.etPhone.setEnabled(!isLoading);
        binding.etEmail.setEnabled(!isLoading);
        binding.etPassword.setEnabled(!isLoading);
        binding.btnRegister.setEnabled(!isLoading);
        binding.btnLogin.setEnabled(!isLoading);

        if (isLoading) {
            binding.btnRegister.setText(R.string.process);
        } else {
            binding.btnRegister.setText(R.string.register);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}