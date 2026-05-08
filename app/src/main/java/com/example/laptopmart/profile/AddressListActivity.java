package com.example.laptopmart.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.databinding.ActivityAddressListBinding;
import com.example.laptopmart.model.UserAddress;

public class AddressListActivity extends AppCompatActivity {

    private ActivityAddressListBinding binding;
    private AddressViewModel viewModel;
    private AddressAdapter adapter;
    private boolean isSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isSelectionMode = getIntent().getBooleanExtra("EXTRA_SELECTION_MODE", false);

        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        setupRecyclerView();
        observeViewModel();
        initButtons();
    }

    private void setupRecyclerView() {
        adapter = new AddressAdapter(new AddressAdapter.OnAddressClickListener() {
            @Override
            public void onAddressClick(UserAddress address) {
                if (isSelectionMode) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("SELECTED_ADDRESS", address);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddressListActivity.this, "Alamat Dipilih: " + address.getLabel(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditClick(UserAddress address) {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                intent.putExtra("EXTRA_ADDRESS", address); // Pass the object to Edit!
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(UserAddress address) {
                viewModel.deleteAddress(address.getId());
            }
        });

        binding.rvAddresses.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getAddresses().observe(this, addresses -> {
            if (addresses != null) {
                adapter.submitList(addresses);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initButtons() {
        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });
    }
}