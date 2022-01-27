package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class HouseHoldRepository {

private static DataFromJson dataFromJson;

    public HouseHoldRepository() throws IOException {
        dataFromJson = new DataFromJson();
    }


    public Map<String, List<Person>> findAll() {

        return dataFromJson.getHouseHold();
    }


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

    public List<Map<String, String>> findChildrenByAddress(String address) {
        List<Map<String, String>> childrenList = new ArrayList<>();
        for(Person person: findByAddress(address)) {
            long age = HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate());


            if(age <= 18) {
                Map<String , String> children = new LinkedHashMap<>();
                children.put("firstName", person.getFirstName());
                children.put("lastName", person.getLastName());
                children.put("age", age + " years");
                childrenList.add(children);
            }
        }
        return childrenList;
    }

    public List<Map<String, String>> findAdultsByAddress(String address) {
        List<Map<String, String>> adultList = new ArrayList<>();
        for(Person person: findByAddress(address)) {
            long age = HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate());

            if(HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate()) > 18) {
                Map<String, String> adult = new LinkedHashMap<>();
                adult.put("firstName", person.getFirstName());
                adult.put("lastName", person.getLastName());
                adult.put("age", age + " years");
                adultList.add(adult);
            }
        }
        return adultList;
    }
}
