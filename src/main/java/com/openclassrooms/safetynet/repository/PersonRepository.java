package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.PersonDAO;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository implements PersonDAO {

     private DataFromJson dataFromJson = new DataFromJson();

     private static List<Person> personList = new ArrayList<>();

     public PersonRepository() throws IOException {
          personList = dataFromJson.getPersons();
     }

     @Override
     public List<Person> findAll() {
          return personList;
     }

     @Override
     public Person findById(int id) {
          for(Person person: personList) {
               if(person.getId() == id) {
                    return person;
               }
          }
          return null;
     }

     @Override
     public Person save(Person person) {
          personList.add(person);

          return person;
     }

     @Override
     public Person update(int id, Person person) {
          personList.set(id, person);
          return findById(id);
     }

     @Override
     public void delete(int id) {
          personList.remove(id);
     }
}
