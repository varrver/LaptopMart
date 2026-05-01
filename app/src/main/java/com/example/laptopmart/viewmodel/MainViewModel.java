package com.example.laptopmart.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.domain.BannerModel;
import com.example.laptopmart.repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainRepository repository = new MainRepository();

    public LiveData<List<BannerModel>> loadBanner() {
        return repository.loadBanner();
    }
}
