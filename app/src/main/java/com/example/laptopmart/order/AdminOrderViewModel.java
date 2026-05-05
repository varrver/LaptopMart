package com.example.laptopmart.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.laptopmart.model.Order;

import java.util.List;

public class AdminOrderViewModel extends ViewModel {

    private final OrderRepository repository;
    private final MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public AdminOrderViewModel() {
        repository = new OrderRepository();
        fetchOrders();
    }

    public LiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private void fetchOrders() {
        repository.listenForAllOrders(new OrderRepository.OrderListCallback() {
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

    public void changeStatus(String orderId, String newStatus) {
        repository.updateOrderStatus(orderId, newStatus, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(String message) {
                toastMessage.setValue(message);
            }

            @Override
            public void onError(String errorMessage) {
                toastMessage.setValue(errorMessage);
            }
        });
    }
}