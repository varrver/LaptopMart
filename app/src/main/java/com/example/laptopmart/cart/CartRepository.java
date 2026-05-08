package com.example.laptopmart.cart;

import com.example.laptopmart.model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public CartRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void addToCart(CartItem newItem, CartCallback callback) {
        if (auth.getCurrentUser() == null) {
            callback.onError("Anda harus login terlebih dahulu!");
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .collection("cart")
                .whereEqualTo("laptopId", newItem.getLaptopId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String existingCartItemId = task.getResult().getDocuments().get(0).getId();
                        int currentQuantity = task.getResult().getDocuments().get(0).getLong("quantity").intValue();

                        firestore.collection("users")
                                .document(userId)
                                .collection("cart")
                                .document(existingCartItemId)
                                .update("quantity", currentQuantity + 1)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("Kuantitas di tambah di keranjang!"))
                                .addOnFailureListener(e -> callback.onError("Gagal menambah kuantitas!"));
                    } else {
                        String newId = firestore.collection("users").document(userId).collection("cart").document().getId();
                        newItem.setId(newId);
                        newItem.setQuantity(1);

                        firestore.collection("users")
                                .document(userId)
                                .collection("cart")
                                .document(newId)
                                .set(newItem)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("Berhasil memasukkan ke keranjang!"))
                                .addOnFailureListener(e -> callback.onError("Gagal memasukkan ke keranjang!"));
                    }
                });
    }

    public void listenForCart(CartListCallback callback) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();
        firestore.collection("users")
                .document(userId)
                .collection("cart")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        callback.onError("Gagal memuat keranjang: " + error.getMessage());
                        return;
                    }
                    if (value != null) {
                        List<CartItem> cartList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            CartItem item = document.toObject(CartItem.class);
                            cartList.add(item);
                        }
                        callback.onDataChange(cartList);
                    }
                });
    }

    public void updateQuantity(String cartItemId, int newQuantity) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();
        if (newQuantity <= 0) {
            deleteCartItem(cartItemId);
            return;
        }

        firestore.collection("users")
                .document(userId)
                .collection("cart")
                .document(cartItemId)
                .update("quantity", newQuantity);
    }

    public void deleteCartItem(String cartItemId) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();
        firestore.collection("users")
                .document(userId)
                .collection("cart")
                .document(cartItemId)
                .delete();
    }

    public interface CartCallback {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public interface CartListCallback {
        void onDataChange(List<CartItem> cartItems);

        void onError(String errorMessage);
    }
}
