package com.example.laptopmart.laptop;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.Laptop;

public class AddEditLaptopViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    private final LaptopRepository repository;

    public AddEditLaptopViewModel() {
        repository = new LaptopRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void saveLaptop(String laptopId, String name, String brand, String description, String priceStr, String stockStr, Uri imageUri, String existingImageUrl) {
        isLoading.setValue(true);

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            Laptop newLaptop = new Laptop(laptopId, name, brand, description, price, stock, existingImageUrl);

            LaptopRepository.LaptopCallBack callback = new LaptopRepository.LaptopCallBack() {
                @Override
                public void onSuccess(String message) {
                    isLoading.setValue(false);
                    toastMessage.setValue(message);
                }

                @Override
                public void onError(String errorMessage) {
                    isLoading.setValue(false);
                    toastMessage.setValue(errorMessage);
                }
            };
            if (imageUri != null) {
                repository.uploadImageAndSaveLaptop(imageUri, newLaptop, callback);
            } else {
                repository.addLaptop(newLaptop, callback);
            }
        } catch (NumberFormatException e) {
            isLoading.setValue(false);
            toastMessage.setValue("Harga dan stok harus berupa angka!");
        }
    }

    public void deleteLaptop(String laptopId){
        isLoading.setValue(true);
        repository.deleteLaptop(laptopId, new LaptopRepository.LaptopCallBack() {
            @Override
            public void onSuccess(String message) {
                isLoading.setValue(false);
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String errorMessage) {
                isLoading.setValue(false);
                toastMessage.setValue(errorMessage);
            }
        });
    }
}
