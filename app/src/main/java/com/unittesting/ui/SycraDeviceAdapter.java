package com.unittesting.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unittesting.databinding.ItemSycraDeviceBinding;

import java.util.List;

public class SycraDeviceAdapter extends
        RecyclerView.Adapter<SycraDeviceAdapter.SycraDeviceViewHolder>{
    //declare a list of devices

    private List<SycraDevice>  devices;

    //Generate constructor with list parameter
    public  SycraDeviceAdapter(List<SycraDevice> devices){
        this.devices = devices;
    }

    // Create a view holder class that extends RecyclerView.ViewHolder
    public static class SycraDeviceViewHolder extends RecyclerView.ViewHolder {
        // Declare a binding variable for each item view
        private ItemSycraDeviceBinding binding;

        // Generate constructor with binding parameter
        public SycraDeviceViewHolder(ItemSycraDeviceBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        // create a method to bind data to views using the binding variable

        public void bind(SycraDevice device){
            binding.deviceNameTextView.setText(device.getName());
            binding.deviceUuidTextView.setText(device.getUuid());
        }
    }

    // Override onCreateViewHolder to inflate the item view using the binding variable
    @NonNull
    @Override
    public SycraDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSycraDeviceBinding binding = ItemSycraDeviceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SycraDeviceViewHolder(binding);
    }

    // Override onBindViewHolder to bind data to views using the view holder
    @Override
    public void onBindViewHolder(@NonNull SycraDeviceViewHolder holder, int position) {
        SycraDevice device = devices.get(position);
        holder.bind(device);
    }

    // Override getItemCount to return the size of the list
    @Override
    public int getItemCount() {
        return devices.size();
    }


}
