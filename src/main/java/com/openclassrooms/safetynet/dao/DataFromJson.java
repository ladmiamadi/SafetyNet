package com.openclassrooms.safetynet.dao;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.openclassrooms.safetynet.model.HouseAddress;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataFromJson {
    private final String fileUrl= "src/main/resources/data.json";
    byte[] bytesFile = Files.readAllBytes(new File(fileUrl).toPath());

    public DataFromJson() throws IOException {
    }

    public List<Person> getPersons() throws IOException {
        JsonIterator jsonIterator = JsonIterator.parse(bytesFile);
        Any any = jsonIterator.readAny();
        Any personAny = any.get("persons");
        List<Person> persons = new ArrayList<>();
        personAny.forEach(a -> persons.add(new Person(persons.size(), a.get("firstName").toString(), a.get("lastName").toString(),
                                a.get("phone").toString(), a.get("email").toString(), a.get("address").toString(),
                                a.get("city").toString(), a.get("zip").toString())));
        return persons;
    }

}
