package com.openclassrooms.safetynet.dao;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataFromJson {
    private final String fileUrl= "src/main/resources/data.json";
    private static byte[] bytesFile;
    private static JsonIterator jsonIterator;
    private static Any buffer;

    public DataFromJson() throws IOException {
        bytesFile = Files.readAllBytes(new File(fileUrl).toPath());
        jsonIterator = JsonIterator.parse(bytesFile);
        buffer = jsonIterator.readAny();
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        Any readPersons = buffer.get("persons");

        readPersons.forEach(a -> persons.add(new Person(persons.size(), a.get("firstName").toString(), a.get("lastName").toString(),
                                a.get("phone").toString(), a.get("email").toString(), a.get("address").toString(),
                                a.get("city").toString(), a.get("zip").toString())));
        return persons;
    }

    public List<FireStation> getFireStations () {
        HashMap<String, List<String>> fireStations= new HashMap<>();
        List<FireStation> fireStationList = new ArrayList<>();

        Any readFireStations = buffer.get("firestations");

        readFireStations.forEach(a -> {
            if(fireStations.containsKey(a.get("station").toString())) {
               fireStations.get(a.get("station").toString()).add(a.get("address").toString());
            } else {
                fireStations.put(a.get("station").toString(), new ArrayList<>());
            }
        });
        for (Map.Entry mapentry : fireStations.entrySet()) {
            fireStationList.add(new FireStation(fireStationList.size(), (String) mapentry.getKey(), (List<String>) mapentry.getValue()));
        }
        return fireStationList;

    }

}
