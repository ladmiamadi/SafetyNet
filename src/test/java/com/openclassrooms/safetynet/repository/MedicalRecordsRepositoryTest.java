package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.MedicalRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MedicalRecordsRepositoryTest {

    @Autowired
    MedicalRecordsRepository medicalRecordsRepository;

    MedicalRecords medicalRecords1 = new MedicalRecords("Ladmiaa", "Madii", "18/07/1985", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    MedicalRecords medicalRecords2 = new MedicalRecords("Bilall", "Madii", "14/02/1984", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    MedicalRecords medicalRecords3 = new MedicalRecords("Anais", "Léa", "14/02/1984", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());

    @Test
    public void findMedicalRecordsList () {
        List medicalRecords = medicalRecordsRepository.findAll();

        assertThat(medicalRecords).isNotNull();
    }

    @Test
    public void findMedicalRecordsWithUnknownFirstNameAndLastNameSuccess () {
        medicalRecordsRepository.save(medicalRecords1);

        MedicalRecords medicalRecords = medicalRecordsRepository.findByFirstNameAndLastName("Ladmiaa", "Madii");

        assertThat(medicalRecords).isNotNull();
    }

    @Test
    public void findMedicalRecordsWithUnknownFirstNameAndLastNameTestFail () {

        MedicalRecords medicalRecords = medicalRecordsRepository.findByFirstNameAndLastName("aa", "bb");

        assertThat(medicalRecords).isNull();
    }


    @Test
    public void deleteMedicalRecordsTest () {
        medicalRecordsRepository.save(medicalRecords3);
        medicalRecordsRepository.delete(medicalRecords3.getFirstName(), medicalRecords3.getLastName());

        assertThat(medicalRecordsRepository.findByFirstNameAndLastName(medicalRecords3.getFirstName(), medicalRecords3.getLastName())).isNull();
    }

    @Test
    public void updateMedicalRecordsTest () {
        medicalRecordsRepository.save(medicalRecords2);
        medicalRecords2.setMedications(null);
        medicalRecords2.setAllergies(null);
        medicalRecords2.setBirthDate("07/31/2015");

        medicalRecordsRepository.update(medicalRecords2.getFirstName(), medicalRecords2.getLastName(), medicalRecords2);

        assertThat(medicalRecordsRepository.findByFirstNameAndLastName(medicalRecords2.getFirstName(), medicalRecords2.getLastName()).getBirthDate()).isEqualTo("07/31/2015");
    }
}
