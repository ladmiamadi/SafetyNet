package com.openclassrooms.safetynet.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;


import java.util.List;

@Data
public class FireStation {

    private int id;
    private String station;
    private List<String> addresses;

    public FireStation(int id, String station, List<String> addresses) {
        this.id = id;
        this.station = station;
        this.addresses = addresses;
    }
}
