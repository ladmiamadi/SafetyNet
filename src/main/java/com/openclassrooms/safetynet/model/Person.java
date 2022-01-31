package com.openclassrooms.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

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

