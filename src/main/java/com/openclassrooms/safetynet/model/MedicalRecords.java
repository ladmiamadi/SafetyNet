package com.openclassrooms.safetynet.model;

import lombok.Data;

import java.util.List;

@Data
public class MedicalRecords {
    private String firstName;
    private String lastName;
    private String birthDate;
    private List<String> medications;
    private List<String> allergies;


    public MedicalRecords(String firstName, String lastName, String birthDate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.medications = medications;
    }
}
