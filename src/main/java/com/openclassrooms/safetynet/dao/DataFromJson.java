package com.openclassrooms.safetynet.dao;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.MedicalRecordsRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Component
public class DataFromJson {
    private static Any buffer;

    public DataFromJson() throws IOException {
        String fileUrl = "src/main/resources/data.json";
        byte[] bytesFile = Files.readAllBytes(new File(fileUrl).toPath());
        JsonIterator jsonIterator = JsonIterator.parse(bytesFile);
        buffer = jsonIterator.readAny();
    }

    private List<String> getList (Any buffer) {

        List<String> medicationsList = new ArrayList<>();
        if(buffer.size()<0) {
            return new ArrayList<>();
        }

        buffer.forEach(medication -> medicationsList.add(medication.toString()));

        return medicationsList;
    }

    public List<Person> getPersons() throws IOException {
        List<Person> persons = new ArrayList<>();
        MedicalRecordsRepository medicalRecordsRepository = new MedicalRecordsRepository();

        Any readPersons = buffer.get("persons");

        readPersons.forEach(a -> {
            MedicalRecords medicalRecords = medicalRecordsRepository.findByFirstNameAndLastName(a.get("firstName").toString(), a.get("lastName").toString());
            persons.add(new Person(a.get("firstName").toString(), a.get("lastName").toString(),
                                a.get("phone").toString(), a.get("email").toString(), a.get("address").toString(),
                                a.get("city").toString(), a.get("zip").toString(), medicalRecords));
        }
        );
        return persons;
    }

    public Map<Integer, FireStation> getFireStations () {
        Map<Integer, FireStation> fireStationsList= new HashMap<>();

        Any readFireStations = buffer.get("firestations");

        readFireStations.forEach(a -> {
            if(fireStationsList.containsKey(a.get("station").toInt())) {
               fireStationsList.get(a.get("station").toInt()).getAddresses().add(a.get("address").toString());
            } else {
                fireStationsList.put(a.get("station").toInt(), new FireStation(a.get("station").toString(), new ArrayList<>()));
                fireStationsList.get(a.get("station").toInt()).getAddresses().add(a.get("address").toString());
            }
        });
        return fireStationsList;
    }

    public List<MedicalRecords> getMedicalRecords () {

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();

        Any readMedicalRecords = buffer.get("medicalrecords");
        readMedicalRecords.forEach(a -> medicalRecordsList.add(new MedicalRecords(a.get("firstName").toString(), a.get("lastName").toString(),
                a.get("birthdate").toString(), getList(a.get("medications")), getList(a.get("allergies")))));

        return medicalRecordsList;
    }

    public Map<String, List<Person>> getHouseHold (){
        Map<String, List<Person>> houseHoldList = new HashMap<>();
        List<Person> personList = null;
        try {
            personList = getPersons();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Person person : personList) {
            if (houseHoldList.containsKey(person.getAddress())) {
                houseHoldList.get(person.getAddress()).add(person);
            } else {
                houseHoldList.put(person.getAddress(), new ArrayList<>());
                houseHoldList.get(person.getAddress()).add(person);
            }
        }

        return houseHoldList;
    }
}
