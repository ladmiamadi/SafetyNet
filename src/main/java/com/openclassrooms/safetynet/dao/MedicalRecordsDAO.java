package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.MedicalRecords;

import java.util.List;

public interface MedicalRecordsDAO {
    List<MedicalRecords> findAll ();

    MedicalRecords findById(int id);

    MedicalRecords save(MedicalRecords medicalRecords);

    MedicalRecords update(int id, MedicalRecords medicalRecords);

    void delete(int id);
}
