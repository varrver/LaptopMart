package com.example.laptopmart.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laptopmart.R;
import com.example.laptopmart.cart.CartAdapter;
import com.example.laptopmart.databinding.ActivityCheckoutBinding;
import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.model.UserAddress;
import com.example.laptopmart.profile.AddressListActivity;
import com.example.laptopmart.profile.AddressViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private AddressViewModel addressViewModel;
    private List<CartItem> cartItems;
    private double itemsPrice;
    private double deliveryPrice = 20000; // Default for "Reguler"
    private UserAddress selectedAddress;

    private final ActivityResultLauncher<Intent> addressLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedAddress = (UserAddress) result.getData().getSerializableExtra("SELECTED_ADDRESS");
                    updateAddressUI();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        
        cartItems = (List<CartItem>) getIntent().getSerializableExtra("CART_ITEMS");
        itemsPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        
        setupDropdowns();
        initOrderSummary();
        initButtons();
        observeViewModel();
    }

    private void observeViewModel() {
        addressViewModel.getAddresses().observe(this, addresses -> {
            if (selectedAddress == null && addresses != null && !addresses.isEmpty()) {
                // Find default or first one
                for (UserAddress addr : addresses) {
                    if (addr.isDefault()) {
                        selectedAddress = addr;
                        break;
                    }
                }
                if (selectedAddress == null) selectedAddress = addresses.get(0);
                updateAddressUI();
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

    private void updateAddressUI() {
        if (selectedAddress != null) {
            binding.tvSelectedLabel.setText(selectedAddress.getLabel());
            binding.tvSelectedReceiver.setText(selectedAddress.getReceiverName() + " | " + selectedAddress.getReceiverPhone());
            binding.tvSelectedAddress.setText(selectedAddress.getFullAddress());
        } else {
            binding.tvSelectedLabel.setText("Belum ada alamat");
            binding.tvSelectedReceiver.setText("Klik 'Pilih Lainnya' untuk menambah");
            binding.tvSelectedAddress.setText("");
        }
    }

    private void initButtons() {
        binding.btnCheckout.setOnClickListener(v -> {
            if (selectedAddress == null) {
                showToast("Harap pilih alamat pengiriman!");
                return;
            }

            String addressStr = selectedAddress.getFullAddress() + " (" + selectedAddress.getReceiverName() + " - " + selectedAddress.getReceiverPhone() + ")";
            showConfirmationDialog(addressStr);
        });
        
        binding.ivBack.setOnClickListener(v -> finish());
        
        binding.tvChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            intent.putExtra("EXTRA_SELECTION_MODE", true);
            addressLauncher.launch(intent);
        });
    }

    private void showConfirmationDialog(String address) {
        String shipping = binding.actvShipping.getText().toString();
        String payment = binding.actvPayment.getText().toString();
        String bankAccount = binding.etBankAccount.getText().toString().trim();
        String notes = binding.etNotes.getText().toString().trim();

        if (payment.isEmpty() || payment.equalsIgnoreCase("Pilih Pembayaran")) {
            showToast("Harap pilih metode pembayaran!");
            return;
        }

        if (!payment.equalsIgnoreCase("COD") && bankAccount.isEmpty()) {
            showToast("Nomor Rekening/E-Wallet wajib diisi untuk metode ini!");
            return;
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi Pesanan")
                .setMessage("Apakah Anda yakin ingin membuat pesanan ini?")
                .setPositiveButton("Ya, Pesan", (dialog, which) -> 
                    checkoutViewModel.createOrder(address, shipping, payment, bankAccount, notes, itemsPrice + deliveryPrice, cartItems))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void setupDropdowns() {
        String[] shippingOptions = {"Reguler", "Ekspres", "Kargo"};
        ArrayAdapter<String> shippingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shippingOptions);
        binding.actvShipping.setAdapter(shippingAdapter);
        binding.actvShipping.setText(shippingOptions[0], false);
        
        binding.actvShipping.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            switch (selected) {
                case "Reguler": deliveryPrice = 20000; break;
                case "Ekspres": deliveryPrice = 50000; break;
                case "Kargo": deliveryPrice = 100000; break;
            }
            updateTotalDisplay();
        });

        String[] paymentOptions = {"Transfer Bank", "E-Wallet", "COD"};
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentOptions);
        binding.actvPayment.setAdapter(paymentAdapter);
        binding.actvPayment.setText(paymentOptions[2], false); // COD is default
        binding.tilBankAccount.setVisibility(View.GONE);

        binding.actvPayment.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            if (selected.equalsIgnoreCase("COD")) {
                binding.tilBankAccount.setVisibility(View.GONE);
            } else {
                binding.tilBankAccount.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initOrderSummary() {
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartClickListener() {
            @Override
            public void onCartClick(CartItem cartItem) {}

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
        updateTotalDisplay();
    }

    private void updateTotalDisplay() {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        binding.tvShippingPrice.setText(formatRupiah.format(deliveryPrice));
        binding.tvCheckoutTotal.setText(formatRupiah.format(itemsPrice + deliveryPrice));
    }

    private void setLoadingState(boolean isLoading) {
        binding.btnCheckout.setEnabled(!isLoading);
        if (isLoading) {
            binding.btnCheckout.setText(R.string.process);
        } else {
            binding.btnCheckout.setText(R.string.checkout);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
