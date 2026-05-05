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
                binding.tvProfilePhone.setText(profile.getPhone());

                // Capitalize the first letter of the role (e.g., "admin" -> "Admin")
                String role = profile.getRole();
                if (role != null && !role.isEmpty()) {
                    String formattedRole = role.substring(0, 1).toUpperCase() + role.substring(1);
                    binding.tvProfileRole.setText(formattedRole);
                }
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButton() {
        binding.btnLogout.setOnClickListener(v -> {
            // 1. Tell ViewModel to clear the Firebase session
            viewModel.logout();

            // 2. Go back to LoginActivity and clear everything else!
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}