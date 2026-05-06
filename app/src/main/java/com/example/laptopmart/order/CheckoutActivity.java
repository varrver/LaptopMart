package com.example.laptopmart.order;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.cart.CartAdapter;
import com.example.laptopmart.databinding.ActivityCheckoutBinding;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.profile.ProfileViewModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private ProfileViewModel profileViewModel;
    private List<CartItem> cartItems;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        cartItems = (List<CartItem>) getIntent().getSerializableExtra("CART_ITEMS");
        totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);

        observeViewModel();
        initButton();
        initOrderSummary();
    }

    private void observeViewModel() {
        profileViewModel.getUserProfileLiveData().observe(this, profile -> {
            if (profile != null && profile.getAddress() != null && !profile.getAddress().isEmpty()) {
                // Automatically fill in the address box so the user doesn't have to type it!
                binding.etAddress.setText(profile.getAddress());
            }
        });
        checkoutViewModel.getIsLoading().observe(this, this::setLoadingState);

        checkoutViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                showToast(message);
                if (message.contains("Berhasil") || message.contains("berhasil")) {
                    finish();
                }
            }
        });
    }

    private void initButton() {
        binding.btnCheckout.setOnClickListener(v -> {
            String address = binding.etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                binding.tilAddress.setError("Alamat tidak boleh kosong!");
                return;
            }
            binding.tilAddress.setError(null);

            checkoutViewModel.createOrder(address, totalPrice, cartItems);
        });
    }

    private void initOrderSummary() {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        binding.tvCheckoutTotal.setText(formatRupiah.format(totalPrice));
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(CartItem cartItem) {

            }

            @Override
            public void onPlusCLick(CartItem cartItem) {
                showToast("Ubah jumlah barang di keranjang!");
            }

            @Override
            public void onMinusClick(CartItem cartItem) {
                showToast("Ubah jumlah barang di keranjang!");
            }
        });
        binding.rvCheckoutItems.setAdapter(adapter);
        binding.rvCheckoutItems.setNestedScrollingEnabled(false);
        adapter.submitList(cartItems);
    }

    private void setLoadingState(boolean isLoading) {
        binding.etAddress.setEnabled(!isLoading);
        binding.btnCheckout.setEnabled(!isLoading);

        if (isLoading) {
            binding.btnCheckout.setText(R.string.process); // "Proses..."
        } else {
            binding.btnCheckout.setText(R.string.checkout);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}