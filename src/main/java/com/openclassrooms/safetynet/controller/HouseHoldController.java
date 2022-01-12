package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.HelperRepository;
import com.openclassrooms.safetynet.repository.HouseHoldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class HouseHoldController {
   @Autowired
    HouseHoldRepository houseHoldRepository;

   @GetMapping("/household")
   MappingJacksonValue getHouseHoldList() {
        return HelperRepository.getFilter("personFilter", Arrays.asList( houseHoldRepository.findAll()), "");
    }
}
