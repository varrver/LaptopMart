package com.example.laptopmart.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.laptopmart.domain.BannerModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public LiveData<List<BannerModel>> loadBanner() {
        MutableLiveData<List<BannerModel>> liveData = new MutableLiveData<>();

        firestore.collection("banners")
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null || snapshot == null) return;

                    List<BannerModel> list = new ArrayList<>();
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        BannerModel item = document.toObject(BannerModel.class);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                    liveData.setValue(list);
                });
        return liveData;
    }
}
