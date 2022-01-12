package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import java.util.Map;

public interface HouseHoldDAO {
    Map<String, List<Person>> findAll();

    List<Person> findByAddress(String address);

    List<Person> findChildrenByAddress(String address);
}
