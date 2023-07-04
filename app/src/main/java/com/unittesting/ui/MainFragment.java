package com.unittesting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.unittesting.R;
import com.unittesting.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.discoverButton.setText("Discover");
        binding.grouping.setText("Grouping");

        //***** on click method that handles discover button ***/
        binding.discoverButton.setOnClickListener(view1 -> {
            // Create and show a toast message for discover button
            Toast.makeText(requireContext(), "You clicked discover",
                    Toast.LENGTH_SHORT).show();
           // Navigation.findNavController(view1).navigate(R.id.action_mainFragment_to_sycraDevicesFragment);
            // Scan for BLE devices
            // scanForBleDevices();

            //action_mainFragment_to_sycraDevicesFragment



            // Navigate to SycraDevicesFragment using the action ID
            NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_mainFragment_to_sycraDevicesFragment);

        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
