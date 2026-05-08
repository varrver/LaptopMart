package com.example.laptopmart.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.auth.LoginActivity;
import com.example.laptopmart.cart.CartActivity;
import com.example.laptopmart.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        observeViewModel();
        initButton();

        return binding.getRoot();
    }

    private void observeViewModel() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                binding.tvProfileName.setText(profile.getName());
                binding.tvProfileEmail.setText(profile.getEmail());

                String role = profile.getRole();
                if (role != null) {
                    if (role.equalsIgnoreCase("admin")) {
                        binding.tvProfileRole.setText("Administrator");
                        binding.menuEditProfile.setVisibility(View.GONE);
                        binding.menuAddress.setVisibility(View.GONE);
                        binding.menuCart.setVisibility(View.GONE);
                        binding.dividerCart.setVisibility(View.GONE);
                    } else {
                        binding.tvProfileRole.setText("Pelanggan");
                        binding.menuEditProfile.setVisibility(View.VISIBLE);
                        binding.menuAddress.setVisibility(View.VISIBLE);
                        binding.menuCart.setVisibility(View.VISIBLE);
                        binding.dividerCart.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) showToast(msg);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButton() {
        // 1. Edit Profile Menu
        binding.menuEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfileDetailActivity.class);
            startActivity(intent);
        });

        binding.menuAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddressListActivity.class);
            startActivity(intent);
        });

        // 2. Cart Menu
        binding.menuCart.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CartActivity.class);
            startActivity(intent);
        });

        // 3. Logout Menu
        binding.menuLogout.setOnClickListener(v -> {
            viewModel.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}