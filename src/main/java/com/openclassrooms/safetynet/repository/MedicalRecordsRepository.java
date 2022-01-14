package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.MedicalRecordsDAO;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class MedicalRecordsRepository implements MedicalRecordsDAO {


    private static List<MedicalRecords> medicalRecordsList = new ArrayList<>();

    public MedicalRecordsRepository() throws IOException {
        DataFromJson dataFromJson = new DataFromJson();
        medicalRecordsList = dataFromJson.getMedicalRecords();
    }

    @Override
    public List<MedicalRecords> findAll() {
        return medicalRecordsList;
    }

    @Override
    public MedicalRecords findByFirstNameAndLastName(String firstName, String lastName) {

        for(MedicalRecords medicalRecords: medicalRecordsList) {
            if(Objects.equals(medicalRecords.getFirstName(), firstName)
                    && Objects.equals(medicalRecords.getLastName(), lastName)) {
                return medicalRecords;
            }
        }
        return null;
    }

    @Override
    public MedicalRecords save(MedicalRecords medicalRecords) {
        medicalRecordsList.add(medicalRecords);

        return medicalRecords;
    }

    public int findIndexOfFirstNameAndLastName(String firstName, String lastName) {
        int index = -1;

        for (MedicalRecords medicalRecords: medicalRecordsList) {
            if(Objects.equals(medicalRecords.getLastName(), lastName) && Objects.equals(medicalRecords.getFirstName(), firstName)) {
                index = medicalRecordsList.indexOf(medicalRecords);
            }
        }
        return index;
    }

    @Override
    public MedicalRecords update(String firstName, String lastName, MedicalRecords medicalRecords) {
        medicalRecordsList.set(findIndexOfFirstNameAndLastName(firstName, lastName), medicalRecords);
        return findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public void delete(String firstName, String lastName) {
        medicalRecordsList.remove(findIndexOfFirstNameAndLastName(firstName, lastName));
    }
}
