package com.example.laptopmart.laptop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.Laptop;

import java.util.List;

public class ListLaptopViewModel extends ViewModel {
    private final MutableLiveData<List<Laptop>> laptopsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> bannersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final LaptopRepository repository;

    public ListLaptopViewModel() {
        repository = new LaptopRepository();
        fetchLaptop();
        fetchBanners();
    }

    public LiveData<List<Laptop>> getLaptopsLiveData() {
        return laptopsLiveData;
    }

    public LiveData<List<String>> getBannersLiveData() {
        return bannersLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchLaptop() {
        repository.listenForLaptops(new LaptopRepository.LaptopListCallback() {
            @Override
            public void onDataChange(List<Laptop> laptops) {
                laptopsLiveData.setValue(laptops);
            }

            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    private void fetchBanners() {
        repository.listenForBanners(new LaptopRepository.BannerListCallback() {
            @Override
            public void onDataChange(List<String> bannerUrls) {
                bannersLiveData.setValue(bannerUrls);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }
}
