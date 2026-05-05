package com.example.laptopmart.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.laptop.LaptopRepository;
import com.example.laptopmart.model.Laptop;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final LaptopRepository repository;

    // We keep a hidden list of ALL laptops in the background
    private List<Laptop> allLaptopsMasterList = new ArrayList<>();

    // This is the list we actually show to the screen
    private final MutableLiveData<List<Laptop>> searchResultsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchViewModel() {
        repository = new LaptopRepository();
        fetchAllLaptops();
    }

    public LiveData<List<Laptop>> getSearchResultsLiveData() { return searchResultsLiveData; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    private void fetchAllLaptops() {
        repository.listenForLaptops(new LaptopRepository.LaptopListCallback() {
            @Override
            public void onDataChange(List<Laptop> laptops) {
                // Save the full list in the background
                allLaptopsMasterList = laptops;
                // Initially, show all laptops!
                searchResultsLiveData.setValue(laptops);
            }

            @Override
            public void onSuccess(String message) { }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    // THE MAGIC SEARCH ENGINE
    public void filterLaptops(String query) {
        if (query == null || query.trim().isEmpty()) {
            // If the search bar is empty, show everything
            searchResultsLiveData.setValue(allLaptopsMasterList);
            return;
        }

        List<Laptop> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(); // Ignore upper/lower case!

        // Loop through all laptops and check if the name or brand contains the typed letters
        for (Laptop laptop : allLaptopsMasterList) {
            if (laptop.getName().toLowerCase().contains(lowerCaseQuery) ||
                    laptop.getBrand().toLowerCase().contains(lowerCaseQuery)) {

                filteredList.add(laptop); // It's a match! Add it to the results.
            }
        }

        // Update the screen with only the matched laptops!
        searchResultsLiveData.setValue(filteredList);
    }
}