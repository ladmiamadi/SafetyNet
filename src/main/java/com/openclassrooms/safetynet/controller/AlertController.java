package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import com.openclassrooms.safetynet.repository.HelperRepository;
import com.openclassrooms.safetynet.repository.HouseHoldRepository;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class AlertController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FireStationRepository fireStationRepository;

    @Autowired
    HouseHoldRepository houseHoldRepository;

    //Récuperer la Liste des habitant couverts par une station
    @GetMapping("/firestations")
    public MappingJacksonValue getCoveredPersonsByFirestation (@RequestParam("stationNumber") Integer stationNumber) {
        List<Object> personsList = new ArrayList<>();
        AtomicInteger adultCount = new AtomicInteger();
        AtomicInteger childCount = new AtomicInteger();

        if(stationNumber != null) {
            FireStation fireStation = fireStationRepository.findById(stationNumber);

            List<String> addresses = new ArrayList<>(fireStation.getAddresses());

            addresses.forEach(address -> personRepository.findByAddress(address).forEach(person -> {
                personsList.add(person);

                long age = HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate());
                if(age <= 18 ) {
                    childCount.getAndIncrement();
                } else {
                    adultCount.getAndIncrement();
                }
            }));
        }

        personsList.add("Number of children: " + childCount);
        personsList.add("Number of adults: " + adultCount);

        return HelperRepository.getFilter("personFilter", personsList, "medicalRecords", "email", "city", "zip");
    }

    //Récuperer la litse d'enfants habitants à une adresse
    @GetMapping("/childAlert")
    public List<Map> getChildrenByAddress(@RequestParam("address") String address) {
        List<Person> members = houseHoldRepository.findByAddress(address);
        List<Map> childrenList = new ArrayList<>();
        Map adultList = new HashMap();
        List adult = new ArrayList();

        for(Person member: members) {
            Map child = new HashMap<>();
            long age = HelperRepository.calculateAge(member.getMedicalRecords().getBirthDate());
            if(age <= 18) {
                child.put("firstName", member.getFirstName());
                child.put("lastName", member.getLastName());
                child.put("age", age + " years");
                childrenList.add(child);
            } else {
                adult.add(member.getFirstName() + " "+ member.getLastName() + " "+ age + " years");
            }
        }
        adultList.put("Other members of houseHold", adult );

        if(childrenList.isEmpty()) {return new ArrayList<>();}
        childrenList.add(adultList);
        return childrenList;
    }

    //Récupérer les numéros de téléphone des personnes desservis par la caserne de pompiers
    @GetMapping("/phoneALert")
    public List<String> phonesList(@RequestParam("firestation") Integer firestation) {
        List<String> phoneList = new ArrayList<>();
        if(firestation != null) {
            FireStation fireStation = fireStationRepository.findById(firestation);

            List<String> addresses = new ArrayList<>(fireStation.getAddresses());
            addresses.forEach(address -> personRepository.findByAddress(address).forEach(person ->
                    phoneList.add(person.getPhone())));
        }
        return phoneList;
    }

    // Récupérer la liste des habitants et la caserne qui les desservit à partir d'une adresse donnée
    @GetMapping("/fire")
    public MappingJacksonValue getPersonsAndFireStationByAddress (@RequestParam("address") String address) {
        if(address != null) {
            List personList = personRepository.findByAddress(address);
            personList.add("couvert(s) par la caserne de pompiers: " + fireStationRepository.findByAddress(address));

            return HelperRepository.getFilter("personFilter", personList, "firstName", "email", "city", "zip");
        }

        return null;
    }

    //Récuperer les habitants d'une addresse desservie par une station de pompiers
    @GetMapping("/flood")
    public Map<String, List> getHouseHoldForFloodALert (@RequestParam("stations") List<String> stations) {
        List<String> addresses = new ArrayList<>();
        Map<String, List> houseHold = new HashMap<>();
        List<Map> persons = new ArrayList();


        if(!stations.isEmpty()) {
            for (String station : stations) {
                addresses.addAll(fireStationRepository.findById(Integer.parseInt(station)).getAddresses());
            }
        }

        for(String address: addresses) {
            houseHoldRepository.findByAddress(address).forEach(inhabitant -> {
                Map person = new LinkedHashMap();
                person.put("firstName", inhabitant.getFirstName());
                person.put("lastName", inhabitant.getLastName());
                person.put("phone", inhabitant.getPhone());
                person.put("age", String.valueOf(HelperRepository.calculateAge(inhabitant.getMedicalRecords().getBirthDate())));
                person.put("medications", inhabitant.getMedicalRecords().getMedications());
                person.put("allergies", inhabitant.getMedicalRecords().getAllergies());

                persons.add(person);
            });
            houseHold.put(address, persons);
        }
        return houseHold;
    }

    //Récupérer les info sur une personne
    @GetMapping("/personInfo")
    public List<Map> getPersonInfo(@RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName) {

        List<Map> personInfoList = new ArrayList<>();
        List<Person> personList = personRepository.findByFirstNameAndLastName(firstName, lastName);

        for(Person person: personList) {
            Map info = new LinkedHashMap();
            long age = HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate());

            info.put("firstName", person.getFirstName());
            info.put("lastName", person.getLastName());
            info.put("address", person.getAddress());
            info.put("age", age + " years");
            info.put("medications", person.getMedicalRecords().getMedications());
            info.put("allergies", person.getMedicalRecords().getAllergies());

            personInfoList.add(info);
        }

        return personInfoList;
    }

    //Récupérer les emails de tous habitants d'une ville'
    @GetMapping("/communityEmail")
    public List<String> emailsList(@RequestParam("city") String city) {
        if(city != null) {
            return personRepository.findEmailsByCity(city);
        } else {
            return null;
        }
    }
}
