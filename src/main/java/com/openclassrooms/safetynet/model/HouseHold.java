package com.openclassrooms.safetynet.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class HouseHold {
    Map<String, List<Person>> member;

    public HouseHold(Map<String, List<Person>> member) {
        this.member = member;
    }
}
