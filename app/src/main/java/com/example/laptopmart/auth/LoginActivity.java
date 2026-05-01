package com.example.laptopmart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.admin.DashboardActivity;
import com.example.laptopmart.databinding.ActivityLoginBinding;
import com.example.laptopmart.home.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initButton();
        observeViewModel();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.checkSession();
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

    private void initButton() {
        binding.btnLogin.setOnClickListener(v -> loginAccount());
        binding.btnRegister.setOnClickListener(v -> goToRegister());
    }

    private void loginAccount() {
        String identifier = binding.etPhoneOrEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (isValidInput(identifier, password)) {
            viewModel.login(identifier, password);
        }
    }

    private boolean isValidInput(String identifier, String password) {
        boolean isValid = true;
        if (identifier.isEmpty()) {
            binding.tilPhoneOrEmail.setError("Nomor telepon/email tidak boleh kosong!");
            isValid = false;
        } else {
            binding.tilPhoneOrEmail.setError(null);
        }
        if (password.isEmpty()) {
            binding.tilPassword.setError("Password tidak boleh kosong!");
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

    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoadingState(boolean isLoading) {
        binding.etPhoneOrEmail.setEnabled(!isLoading);
        binding.etPassword.setEnabled(!isLoading);
        binding.btnLogin.setEnabled(!isLoading);
        binding.btnRegister.setEnabled(!isLoading);

        if (isLoading) {
            binding.btnLogin.setText(R.string.process);
        } else {
            binding.btnLogin.setText(R.string.login);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}