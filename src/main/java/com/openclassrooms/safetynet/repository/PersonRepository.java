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
     public Person findByFirstNameAndLastName(String firstName, String lastName) {
          for(Person person: personList) {
               if(Objects.equals(person.getFirstName(), firstName) && Objects.equals(person.getLastName(), lastName)) {
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

     public int findIndexOfFirstNameAndLastName(String firstName, String lastName) {
          int index = -1;

          for (Person person: personList) {
               if(Objects.equals(person.getLastName(), lastName) && Objects.equals(person.getFirstName(), firstName)) {
                    index = personList.indexOf(person);
               }
          }
          return index;
     }

     @Override
     public Person update(String firstName, String lastName, Person person) {
          personList.set(findIndexOfFirstNameAndLastName(firstName, lastName), person);
          return findByFirstNameAndLastName(firstName, lastName);
     }

     @Override
     public void delete(String firstName, String lastName) {
          personList.remove(findIndexOfFirstNameAndLastName(firstName, lastName));
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


}
