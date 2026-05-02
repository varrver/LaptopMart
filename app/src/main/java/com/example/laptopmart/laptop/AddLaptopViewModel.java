package com.example.laptopmart.laptop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.Laptop;

public class AddLaptopViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    private final LaptopRepository repository;

    public AddLaptopViewModel() {
        repository = new LaptopRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void saveLaptop(String name, String brand, String description, String priceStr, String stockStr, String imageUrl) {
        isLoading.setValue(true);

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            Laptop newLaptop = new Laptop("", name, brand, description, price, stock, imageUrl);
            repository.addLaptop(newLaptop, new LaptopRepository.LaptopCallBack() {
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
        } catch (NumberFormatException e) {
            isLoading.setValue(false);
            toastMessage.setValue("Harga dan stok harus berupa angka!");
        }
    }
}
