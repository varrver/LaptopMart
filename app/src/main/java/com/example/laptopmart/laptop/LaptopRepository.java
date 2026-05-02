package com.example.laptopmart.laptop;

import com.example.laptopmart.model.Laptop;
import com.google.firebase.firestore.FirebaseFirestore;

public class LaptopRepository {
    private final FirebaseFirestore firestore;

    public LaptopRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public interface LaptopCallBack {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public void addLaptop(Laptop laptop, LaptopCallBack callBack) {
        String newId = firestore.collection("laptops").document().getId();

        laptop.setId(newId);

        firestore.collection("laptops")
                .document(newId)
                .set(laptop)
                .addOnSuccessListener(aVoid -> callBack.onSuccess("Berhasil menambahkan laptop!"))
                .addOnFailureListener(e -> {
                    String error = e.getMessage() != null ?
                            e.getMessage() : "Gagal menambahkan laptop!";
                    callBack.onError(error);
                });
    }
}
