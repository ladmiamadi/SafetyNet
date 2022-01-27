package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
    Person person2 = new Person("John", "Doe", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
    Person person3 = new Person("John", "Boyd", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);


    private List<Person> personList = Arrays.asList(person1, person2, person3);

    @MockBean
    DataFromJson dataFromJson;

    @Test
    public void findByNameAndLastNameSuccessTest () throws Exception {

        personRepository.save(person1);

        Person personResult = personRepository.findByFirstNameAndLastName(personList.get(0).getFirstName(), personList.get(0).getLastName());

        assertThat(personResult).isEqualTo(person1);
    }

    @Test
    public void findByNameAndLastNameShouldReturnNullTest () throws Exception {

        personRepository.save(person1);

        Person personResult = personRepository.findByFirstNameAndLastName(person2.getFirstName(), person2.getLastName());

        assertThat(personResult).isEqualTo(null);
    }

    @Test
    public void findAllSuccessTest () throws Exception {

        List personResult = personRepository.findAll();
        assertThat(personResult).isNotNull();
    }

    @Test
    public void findIndexOfFirstNameAndLastNameSuccessTest () throws Exception {
        personRepository.save(person1);

        int index = personRepository.findIndexOfFirstNameAndLastName(person1.getFirstName(), person1.getLastName());

        assertThat(index).isGreaterThan(-1);
    }

    @Test
    public void updatePersonTest () throws Exception {
        personRepository.save(person2);
        person2.setAddress("11 Rue Clichy");
        person2.setCity("Alger");
        person2.setEmail("lea.anais2015@gmail.com");
        person2.setPhone("1515");
        person2.setZip("95370");
        person2.setMedicalRecords(null);

        personRepository.update("John", "Doe", person2);

        assertThat(personRepository.findByFirstNameAndLastName("John", "Doe").getCity()).isEqualTo("Alger");
    }

    @Test
    public void deletePersonTest () throws Exception {
        personRepository.delete("John", "Boyd");

        assertThat(personRepository.findIndexOfFirstNameAndLastName("John", "Boyd")).isEqualTo(-1);
    }

    @Test
    public void findEmailsByCityTest () throws Exception {
        personRepository.save(person1);

        assertThat(personRepository.findEmailsByCity("Culver").get(23)).isEqualTo("ladmia.madi@gmail.com");
    }

    @Test
    public void findByAddressTest () throws Exception {
        personRepository.save(person1);

        assertThat(personRepository.findByAddress(person1.getAddress())).isNotNull();
    }

}
