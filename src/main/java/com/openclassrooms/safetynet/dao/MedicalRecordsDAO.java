package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.MedicalRecords;

import java.util.List;

public interface MedicalRecordsDAO {
    List<MedicalRecords> findAll ();

    MedicalRecords findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecords save(MedicalRecords medicalRecords);

    MedicalRecords update(String firstName, String lastName, MedicalRecords medicalRecords);

    void delete(String firstName, String lastName);
}
