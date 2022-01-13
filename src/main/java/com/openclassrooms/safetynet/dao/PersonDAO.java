package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.Person;

import java.util.List;

public interface PersonDAO{
    List<Person> findAll ();

    Person save(Person person);

    Person update(String firstName, String lastName, Person person);

    void delete(String firstName, String lastName);

    List<String> findEmailsByCity (String city);

    List<Person> findByAddress(String Address);

    Person findByFirstNameAndLastName(String firstName, String lastName);
}
