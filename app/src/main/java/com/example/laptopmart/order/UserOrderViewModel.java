package com.example.laptopmart.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.laptopmart.model.Order;
import java.util.List;

public class UserOrderViewModel extends ViewModel {

    private final OrderRepository repository;
    private final MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public UserOrderViewModel() {
        repository = new OrderRepository();
        fetchMyOrders();
    }

    public LiveData<List<Order>> getOrdersLiveData() { return ordersLiveData; }
    public LiveData<String> getToastMessage() { return toastMessage; }

    private void fetchMyOrders() {
        // CALL THE NEW REPOSITORY METHOD!
        repository.listenForUserOrders(new OrderRepository.OrderListCallback() {
            @Override
            public void onDataChange(List<Order> orders) {
                ordersLiveData.setValue(orders);
            }

            @Override
            public void onError(String errorMessage) {
                toastMessage.setValue(errorMessage);
            }
        });
    }
}