package com.example.laptopmart.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laptopmart.databinding.ViewHolderAddressBinding;
import com.example.laptopmart.model.UserAddress;

import java.util.Objects;

public class AddressAdapter extends ListAdapter<UserAddress, AddressAdapter.AddressViewHolder> {

    private final OnAddressClickListener listener;

    public interface OnAddressClickListener {
        void onAddressClick(UserAddress address);
        void onEditClick(UserAddress address);
        void onDeleteClick(UserAddress address);
    }

    public AddressAdapter(OnAddressClickListener listener) {
        super(new DiffUtil.ItemCallback<UserAddress>() {
            @Override
            public boolean areItemsTheSame(@NonNull UserAddress oldItem, @NonNull UserAddress newItem) {
                return Objects.equals(oldItem.getId(), newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull UserAddress oldItem, @NonNull UserAddress newItem) {
                return oldItem.isDefault() == newItem.isDefault() &&
                        Objects.equals(oldItem.getLabel(), newItem.getLabel()) &&
                        Objects.equals(oldItem.getFullAddress(), newItem.getFullAddress()) &&
                        Objects.equals(oldItem.getReceiverName(), newItem.getReceiverName()) &&
                        Objects.equals(oldItem.getReceiverPhone(), newItem.getReceiverPhone());
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(ViewHolderAddressBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderAddressBinding binding;

        public AddressViewHolder(ViewHolderAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserAddress address, OnAddressClickListener listener) {
            binding.tvLabel.setText(address.getLabel());
            binding.tvReceiverName.setText(address.getReceiverName());
            binding.tvReceiverPhone.setText(address.getReceiverPhone());
            binding.tvFullAddress.setText(address.getFullAddress());
            binding.tvDefaultBadge.setVisibility(address.isDefault() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> listener.onAddressClick(address));
            binding.btnEdit.setOnClickListener(v -> listener.onEditClick(address));
            binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(address));
        }
    }
}
