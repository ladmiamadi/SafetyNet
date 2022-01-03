package com.openclassrooms.safetynet.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class FireStation {
    private int station;
    private List<HouseAddress> addresses;
}
