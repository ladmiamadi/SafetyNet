package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.PersonDAO;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PersonRepository implements PersonDAO {

     private static List<Person> personList = new ArrayList<>();

     public PersonRepository() throws IOException {
          DataFromJson dataFromJson = new DataFromJson();
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

     @Override
     public List<String> findEmailsByCity(String city) {
          List<String> emailsList = new ArrayList<>();
          for(Person person: personList) {
               if(Objects.equals(person.getCity(), city)) {
                    emailsList.add(person.getEmail());
               }
          }
          return emailsList;
     }

     @Override
     public List<Person> findByAddress(String address) {

          List<Person> persons = new ArrayList<>();
          personList.forEach(person -> {

               if(Objects.equals(person.getAddress(), address)) {
                    persons.add(person);
               }
          });

          return persons;
     }

     @Override
     public List<Person> findByFirstNameAndLastName(String firstName, String lastName) {
          List<Person> filteredList = new ArrayList();
          for(Person person: personList) {
               if(Objects.equals(person.getFirstName(), firstName) && Objects.equals(person.getLastName(), lastName)) {
                    filteredList.add(person);
               }
          }
          return filteredList;
     }
}
