package com.openclassrooms.safetynet.model;

import lombok.Data;


import java.util.List;

@Data
public class FireStation {

    private String station;
    private List<String> addresses;

    public FireStation(String station, List<String> addresses){
        this.station = station;
        this.addresses = addresses;
    }
}
