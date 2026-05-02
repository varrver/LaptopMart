package com.example.laptopmart.laptop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.laptopmart.databinding.FragmentListLaptopBinding;

public class ListLaptopFragment extends Fragment {

    private FragmentListLaptopBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListLaptopBinding.inflate(inflater, container, false);

        initButton();

        return binding.getRoot();
    }

    private void initButton() {
        binding.btnAdd.setOnClickListener(v -> navigateToAddLaptop());
    }

    private void navigateToAddLaptop() {
        Intent intent = new Intent(requireContext(), AddLaptopActivity.class);
        startActivity(intent);
    }
}