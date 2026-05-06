package com.example.laptopmart.laptop;

import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.laptopmart.model.Laptop;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LaptopRepository {
    private final FirebaseFirestore firestore;

    public LaptopRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public interface LaptopCallBack {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public interface LaptopListCallback {
        void onDataChange(List<Laptop> laptops);

        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public interface SingleLaptopCallback {
        void onSuccess(Laptop laptop);

        void onError(String errorMessage);
    }

    public interface BannerListCallback {
        void onDataChange(List<String> bannerUrls);

        void onError(String errorMessage);
    }

    public void uploadImageAndSaveLaptop(Uri imageUri, Laptop laptop, LaptopCallBack callBack) {
        String cleanName = laptop.getName().replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        String customFileName = cleanName + "_" + System.currentTimeMillis();
        MediaManager.get().upload(imageUri)
                .unsigned("laptopmart")
                .option("folder", "laptops")
                .option("public_id", customFileName)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        laptop.setImageUrl(imageUrl);
                        addLaptop(laptop, callBack);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        callBack.onError("Gagal mengunggah gambar: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                    }
                }).dispatch();
    }

    public void addLaptop(Laptop laptop, LaptopCallBack callBack) {
        String id = laptop.getId();

        if (id == null || id.isEmpty()) {
            id = firestore.collection("laptops").document().getId();
            laptop.setId(id);
        }

        firestore.collection("laptops")
                .document(id)
                .set(laptop)
                .addOnSuccessListener(aVoid -> callBack.onSuccess("Berhasil menyimpan laptop!"))
                .addOnFailureListener(e -> {
                    String error = e.getMessage() != null ?
                            e.getMessage() : "Gagal menyimpan laptop!";
                    callBack.onError(error);
                });
    }

    public void listenForLaptops(LaptopListCallback callback) {
        firestore.collection("laptops")
                .addSnapshotListener(((value, error) -> {
                    if (error != null) {
                        callback.onError("Gagal mengambil data: " + error.getMessage());
                        return;
                    }
                    if (value != null) {
                        List<Laptop> laptopList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Laptop laptop = document.toObject(Laptop.class);
                            laptopList.add(laptop);
                        }
                        callback.onDataChange(laptopList);
                    }
                }));
    }

    public void deleteLaptop(String laptopId, LaptopCallBack callBack) {
        firestore.collection("laptops")
                .document(laptopId)
                .delete()
                .addOnSuccessListener(aVoid -> callBack.onSuccess("Berhapus menghapus"))
                .addOnFailureListener(e -> callBack.onError("Gagal meyimpan laptop!"));
    }

    public void getLaptopById(String laptopId, SingleLaptopCallback callback) {
        firestore.collection("laptops").document(laptopId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Laptop laptop = documentSnapshot.toObject(Laptop.class);
                        callback.onSuccess(laptop);
                    } else {
                        callback.onError("Laptop tidak ditemukan di database!");
                    }
                })
                .addOnFailureListener(e -> callback.onError("Gagal mengambil data: " + e.getMessage()));
    }

    public void listenForBanners(BannerListCallback callback) {
        firestore.collection("banners").addSnapshotListener((value, error) -> {
            if (error != null) {
                callback.onError("Gagal mengambil banner: " + error.getMessage());
                return;
            }
            if (value != null) {
                List<String> urls = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    // Change "imageUrl" if your field name is different in Firestore!
                    String url = doc.getString("url");
                    if (url != null) {
                        urls.add(url);
                    }
                }
                callback.onDataChange(urls);
            }
        });
    }
}
