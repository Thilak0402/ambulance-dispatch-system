package com.ambulance.model;

public class Driver {
    private String driverId;
    private String name;
    private String contactNumber;

    public Driver(String driverId, String name, String contactNumber) {
        this.driverId = driverId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getName() { return name; }
}