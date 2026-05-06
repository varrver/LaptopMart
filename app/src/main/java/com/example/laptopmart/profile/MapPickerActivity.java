package com.example.laptopmart.profile;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laptopmart.R;
import com.example.laptopmart.databinding.ActivityMapPickerBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// MUST implement OnMapReadyCallback!
public class MapPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMapPickerBinding binding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapPickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Connect the map fragment!
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Tells the map to load in the background
        }

        binding.btnSelectLocation.setOnClickListener(v -> getAddressFromMap());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location to Indonesia (Jakarta) so it doesn't start in the middle of the ocean!
        LatLng defaultLocation = new LatLng(-6.2088, 106.8456);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f));
    }

    private void getAddressFromMap() {
        if (mMap == null) return;

        binding.btnSelectLocation.setText("Mencari Alamat...");
        binding.btnSelectLocation.setEnabled(false);

        // 1. Get the exact coordinates of the center of the screen
        LatLng centerLatLng = mMap.getCameraPosition().target;

        // 2. Use Geocoder to translate coordinates into words
        Geocoder geocoder = new Geocoder(this, new Locale("id", "ID"));

        try {
            // Ask Google for 1 address matching these coordinates
            List<Address> addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                // We found it! Grab the full address string.
                String fullAddress = addresses.get(0).getAddressLine(0);

                // 3. Send it back to the ProfileDetailActivity!
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_ADDRESS", fullAddress);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close the map
            } else {
                Toast.makeText(this, "Alamat tidak ditemukan, geser peta sedikit.", Toast.LENGTH_SHORT).show();
                resetButton();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Gagal mengambil alamat. Periksa koneksi internet.", Toast.LENGTH_SHORT).show();
            resetButton();
        }
    }

    private void resetButton() {
        binding.btnSelectLocation.setText("Pilih Lokasi Ini");
        binding.btnSelectLocation.setEnabled(true);
    }
}