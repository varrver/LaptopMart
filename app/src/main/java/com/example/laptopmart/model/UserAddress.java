package com.example.laptopmart.model;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class UserAddress implements Serializable {
    private String id;
    private String label; // e.g., "Rumah", "Kantor"
    private String receiverName;
    private String receiverPhone;
    private String fullAddress;
    private boolean isDefault;

    public UserAddress() {
    }

    public UserAddress(String id, String label, String receiverName, String receiverPhone, String fullAddress, boolean isDefault) {
        this.id = id;
        this.label = label;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.fullAddress = fullAddress;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @PropertyName("isDefault")
    public boolean isDefault() {
        return isDefault;
    }

    @PropertyName("isDefault")
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
