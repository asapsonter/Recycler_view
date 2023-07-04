package com.unittesting.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unittesting.R;
import com.unittesting.databinding.FragmentSycraDevicesBinding;

import java.util.List;

public class SycraDevicesFragment extends Fragment {
    //declare a binding variable for the fragment and view model
    private SycraDevicesViewModel viewModel;

    private FragmentSycraDevicesBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSycraDevicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the view model using the ViewModelProvider
        viewModel = new ViewModelProvider(this).get(SycraDevicesViewModel.class);
        // Observe the live data of devices from the view model
        viewModel.getDevices().observe(getViewLifecycleOwner(),
        new Observer<List<SycraDevice>>(){

            @Override
            public void onChanged(List<SycraDevice> devices) {
            // Create an adapter with the devices list and set it to the recycler view

                SycraDeviceAdapter adapter = new SycraDeviceAdapter(devices);
                binding.devicesRecyclerView.setAdapter(adapter);

            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set the binding variable to null when the view is destroyed
        binding = null;
    }

}