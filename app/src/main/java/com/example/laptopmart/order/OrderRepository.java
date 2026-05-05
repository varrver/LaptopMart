package com.example.laptopmart.order;

import com.example.laptopmart.model.CartItem;
import com.example.laptopmart.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

public class OrderRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public OrderRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public interface OrderCallback {
        void onSuccess(String message);

        void onError(String message);
    }

    public void placeOrder(String address, double totalPrice, List<CartItem> cartItems, OrderCallback callback) {
        if (auth.getCurrentUser() == null) {
            callback.onError("Anda harus login terlebih dahulu");
            return;
        }
        String userId = auth.getCurrentUser().getUid();
        firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String customerName = documentSnapshot.getString("name");
                    if (customerName == null) customerName = "Customer";
                    DocumentReference orderReference = firestore.collection("orders").document();
                    Order order = new Order(
                            orderReference.getId(),
                            userId,
                            customerName,
                            address,
                            totalPrice,
                            "Menunggu",
                            System.currentTimeMillis(),
                            cartItems
                    );
                    WriteBatch batch = firestore.batch();
                    batch.set(orderReference, order);
                    for (CartItem item : cartItems) {
                        DocumentReference cartItemReference = firestore.collection("users")
                                .document(userId)
                                .collection("cart")
                                .document(item.getId());
                        batch.delete(cartItemReference);
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> callback.onSuccess("Pesanan berhasil dibuat!"))
                            .addOnFailureListener(e -> callback.onError("Gagal membuat pesanan!"));
                })
                .addOnFailureListener(e -> callback.onError("Gagal mengambil data user!"));
    }
}
