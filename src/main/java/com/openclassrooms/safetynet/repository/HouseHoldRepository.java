package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.HouseHoldDAO;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class HouseHoldRepository implements HouseHoldDAO {

private static DataFromJson dataFromJson;

    public HouseHoldRepository() throws IOException {
        dataFromJson = new DataFromJson();
    }

    @Override
    public Map<String, List<Person>> findAll() {

        return dataFromJson.getHouseHold();
    }

    @Override
    public List<Person> findByAddress(String address) {
        Map<String, List<Person>> houseHold = findAll();
        List<Person> members = new ArrayList<>();

        for(Map.Entry<String, List<Person>> mapEntry: houseHold.entrySet()) {
            if(Objects.equals(mapEntry.getKey(), address)) {
                members= mapEntry.getValue();
            }
        }
        return  members;
    }

    @Override
    public List<Person> findChildrenByAddress(String address) {
        List<Person> childrenList = new ArrayList<>();
        for(Person person: findByAddress(address)) {
            if(HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate()) <= 18) {
                childrenList.add(person);
            }
        }

        return childrenList;
    }
}
