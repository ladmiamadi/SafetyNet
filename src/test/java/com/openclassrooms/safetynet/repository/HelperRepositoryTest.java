package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HelperRepositoryTest {

    @Test
    public void testCalculateAge() {
        long result = HelperRepository.calculateAge("07/18/1985");

        assertThat(result).isEqualTo(36);
    }

    @Test
    public void countChildrenAndAdultsTest () {
        MedicalRecords medicalRecords1 = new MedicalRecords("Ladmia", "Madii", "07/18/1985", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
        MedicalRecords medicalRecords2 = new MedicalRecords("Bilal", "Madii", "02/14/1984", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
        MedicalRecords medicalRecords3 = new MedicalRecords("John", "Boyd", "01/02/2015", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());

        Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", medicalRecords1);
        Person person2 = new Person("John", "Doe", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", medicalRecords2);
        Person person3 = new Person("John", "Boyd", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", medicalRecords3);


        List<Person> members = Arrays.asList(person1, person2, person3);

        Map<String, Integer> result = HelperRepository.countChildrenAndAdults(members);

        assertThat(result.containsValue(2)).isTrue();
        assertThat(result.containsValue(1)).isTrue();
    }
}
