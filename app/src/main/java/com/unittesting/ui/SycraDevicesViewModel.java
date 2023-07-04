package com.unittesting.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SycraDevicesViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //declare a mutable live data of devices
    private MutableLiveData<List<SycraDevice>> devices;

    // generate a getter for livedata
     public MutableLiveData<List<SycraDevice>> getDevices(){
         if (devices == null){
             devices = new MutableLiveData<>();
             loadDevices();
         }
         return devices;
     }

    // Create a method to load dummy data of devices and post it to the live data
    private void loadDevices() {
         List<SycraDevice> dummyDevices = new ArrayList<>();
         dummyDevices.add(new SycraDevice("Sycra One", "1234-5678-90AB-CDEF"));
         dummyDevices.add(new SycraDevice("Sycra Two", "2345-6789-0ABC-DEF1"));
         dummyDevices.add(new SycraDevice("Sycra Three", "3456-7890-ABCD-EF12"));
         devices.postValue(dummyDevices);
    }

}