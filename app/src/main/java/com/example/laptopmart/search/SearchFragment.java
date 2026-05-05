package com.example.laptopmart.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.FragmentSearchBinding;
import com.example.laptopmart.laptop.DetailLaptopActivity;
import com.example.laptopmart.laptop.LaptopAdapter;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private LaptopAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupRecyclerView();
        observeViewModel();
        setupSearchBar();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        // Reuse our awesome LaptopAdapter!
        adapter = new LaptopAdapter(laptop -> {
            // Open the Detail Screen when clicked
            Intent intent = new Intent(requireContext(), DetailLaptopActivity.class);
            intent.putExtra("LAPTOP_ID", laptop.getId());
            intent.putExtra("LAPTOP_NAME", laptop.getName());
            intent.putExtra("LAPTOP_BRAND", laptop.getBrand());
            intent.putExtra("LAPTOP_DESCRIPTION", laptop.getDescription());
            intent.putExtra("LAPTOP_PRICE", laptop.getPrice());
            intent.putExtra("LAPTOP_STOCK", laptop.getStock());
            intent.putExtra("LAPTOP_IMAGE", laptop.getImageUrl());
            startActivity(intent);
        });

        binding.rvSearchResults.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), laptops -> {
            if (laptops != null) {
                adapter.submitList(laptops); // DiffUtil will animate the search results!
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchBar() {
        // Listen to every keystroke in the EditText
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Send the typed text to the ViewModel to filter the list!
                viewModel.filterLaptops(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}