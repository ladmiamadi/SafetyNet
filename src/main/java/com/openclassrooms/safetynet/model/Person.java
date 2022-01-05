package com.openclassrooms.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@JsonFilter("monFiltre")
public class Person {

    //@JsonIgnore
    private int id;

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String zip;
    private MedicalRecords medicalRecords;

    public Person(int id, String firstName, String lastName, String phone, String email, String address, String city, String zip) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address= address;
        this.city = city;
        this.zip = zip;
    }
}
