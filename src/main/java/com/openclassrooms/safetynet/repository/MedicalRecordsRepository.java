package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.MedicalRecordsDAO;
import com.openclassrooms.safetynet.model.MedicalRecords;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class MedicalRecordsRepository implements MedicalRecordsDAO {
    private DataFromJson dataFromJson = new DataFromJson();

    public MedicalRecordsRepository() throws IOException {
    }

    @Override
    public List<MedicalRecords> findAll() {
        return dataFromJson.getMedicalRecords();
    }

    @Override
    public MedicalRecords findById(int id) {
        return null;
    }

    @Override
    public MedicalRecords save(MedicalRecords medicalRecords) {
        return null;
    }

    @Override
    public MedicalRecords update(int id, MedicalRecords medicalRecords) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
