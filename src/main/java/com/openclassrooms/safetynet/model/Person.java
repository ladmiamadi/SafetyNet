package com.openclassrooms.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@JsonFilter("personFilter")
public class Person {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String zip;
    private MedicalRecords medicalRecords;

    public Person(String firstName, String lastName, String phone, String email,
                  String address, String city, String zip, MedicalRecords medicalRecords) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address= address;
        this.city = city;
        this.zip = zip;
        this.medicalRecords = medicalRecords;
    }
}

