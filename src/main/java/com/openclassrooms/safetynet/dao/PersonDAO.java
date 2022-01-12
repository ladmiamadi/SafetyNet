package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.Person;

import java.util.List;

public interface PersonDAO{
    List<Person> findAll ();

    Person findById(int id);

    Person save(Person person);

    Person update(int id, Person person);

    void delete(int id);

    List<String> findEmailsByCity (String city);

    List<Person> findByAddress(String Address);

    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
}
