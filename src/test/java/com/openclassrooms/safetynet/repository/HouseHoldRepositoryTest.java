package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class HouseHoldRepositoryTest {

    @MockBean
    DataFromJson dataFromJson;

    @Autowired
    HouseHoldRepository houseHoldRepository;

    MedicalRecords medicalRecords1 = new MedicalRecords("Ladmia", "Madi", "07/18/2015", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", medicalRecords1);

    @Test
    public void getHouseHoldListTest () throws Exception {
        Map houseHoldResult = houseHoldRepository.findAll();
        assertThat(houseHoldResult).isNotNull();
    }


    @Test
    public void findByAddressTest () throws Exception {

        List<Person> person = houseHoldRepository.findByAddress(person1.getAddress());

        assertThat(person).isNotNull();
    }

    @Test
    public void findChildrenByAddressTest () throws Exception {

        List<Map<String, String>> childrenList = houseHoldRepository.findChildrenByAddress("1509 Culver St");

        String lastName = childrenList.get(0).get("lastName");

        assertThat(lastName).isEqualTo("Boyd");
    }

    @Test
    public void findAdultsByAddressTest () throws Exception {

        List<Map<String, String>> childrenList = houseHoldRepository.findAdultsByAddress("1509 Culver St");

        String lastName = childrenList.get(0).get("lastName");

        assertThat(lastName).isEqualTo("Boyd");
    }


}
