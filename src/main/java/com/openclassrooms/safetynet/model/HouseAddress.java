package com.openclassrooms.safetynet.model;

import lombok.Data;

@Data
public class HouseAddress {
    private String address;
    private String city;
    private String zip;

    public HouseAddress(String address, String city, String zip) {
        this.address = address;
        this.city = city;
        this.zip = zip;
    }
}
