package com.unittesting.ui;

public class SycraDevice {
    // Declare fields for name and UUID
    private String name; private String uuid;

    //generate constructor, getters & setters
    public SycraDevice(String name, String uuid){
        this.name = name;
        this.uuid = uuid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
