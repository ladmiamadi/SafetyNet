package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import com.openclassrooms.safetynet.repository.HouseHoldRepository;
import com.openclassrooms.safetynet.repository.MedicalRecordsRepository;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebMvcTest(controllers = AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicalRecordsRepository medicalRecordsRepository;

    @MockBean
    private HouseHoldRepository houseHoldRepository;

    Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
    Person person2 = new Person("John", "Doe", "0605612151", "ladmia.madi@gmail.com", "Place de Clichy", "Culver", "95370", null);

    FireStation fireStation1 = new FireStation("1", Arrays.asList("Rue Des 24 Arpents", "Rue Rivoli"));
    FireStation fireStation2 = new FireStation("2", Arrays.asList("Place de Clichy", "Bv Victor Bordier"));

    MedicalRecords medicalRecords1 = new MedicalRecords("Ladmia", "Madi", "18/07/1985", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    MedicalRecords medicalRecords2 = new MedicalRecords("John", "Doe", "14/02/1984", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());

    private List<Person> personList = Arrays.asList(person1, person2);
    private Map<String, FireStation> fireStationMap = Map.of(fireStation1.getStation(), fireStation1, fireStation2.getStation(), fireStation2);
    private List<MedicalRecords> medicalRecordsList= Arrays.asList(medicalRecords1, medicalRecords2);

}
