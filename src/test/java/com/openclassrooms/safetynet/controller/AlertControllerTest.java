package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private HouseHoldRepository houseHoldRepository;


    MedicalRecords medicalRecords1 = new MedicalRecords("Ladmia", "Madi", "07/18/1985", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    MedicalRecords medicalRecords3 = new MedicalRecords("Tanley", "Boyd", "02/14/2015", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());


    Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", medicalRecords1);
    Person person3 = new Person("Tenley", "Boyd", "0605612151", "ladmia.madi@gmail.com", "Place de Clichy", "Culver", "95370", null);

    FireStation fireStation1 = new FireStation("1", Arrays.asList("Rue Des 24 Arpents", "Rue Rivoli"));


    @Test
    public void getCoveredPersonsByFireStationTestSuccess () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(fireStation1);

        mockMvc.perform(get("/firestations")
                        .param("stationNumber", fireStation1.getStation())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getCoveredPersonsByUnknownFireStationTestFail () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(null);

        mockMvc.perform(get("/firestations")
                        .param("stationNumber", fireStation1.getStation())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getChildrenByAddressTestSuccess () throws Exception {
        Map<String, String> childrenList = new LinkedHashMap<>();
        childrenList.put("firstName", person3.getFirstName());
        childrenList.put("lastName", person3.getLastName());
        childrenList.put("age", String.valueOf(HelperRepository.calculateAge(medicalRecords3.getBirthDate())));
        Mockito.when(houseHoldRepository.findChildrenByAddress(person3.getAddress())).thenReturn(List.of(childrenList));

        mockMvc.perform(get("/childAlert")
                        .param("address", person3.getAddress())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getChildrenByUnknownAddressTestFail () throws Exception {
        Mockito.when(houseHoldRepository.findChildrenByAddress(person3.getAddress())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/childAlert")
                        .param("address", person3.getAddress())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(0)));
    }

    @Test
    public void getPhonesListTestSuccess () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()))).thenReturn(fireStation1);
        Mockito.when(personRepository.findByAddress(fireStation1.getAddresses().get(0))).thenReturn(List.of(person1));

        mockMvc.perform(get("/phoneALert")
                        .param("firestation", fireStation1.getStation())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getPhonesListWithUnknownStationTestFail () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()))).thenReturn(null);

        mockMvc.perform(get("/phoneALert")
                        .param("firestation", fireStation1.getStation())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void gtePersonAndFireStationByAddressTestSuccess () throws Exception {
        Mockito.when(personRepository.findByAddress(person1.getAddress())).thenReturn(List.of(person1));

        Mockito.when(fireStationRepository.findByAddress(person1.getAddress())).thenReturn(Integer.valueOf(fireStation1.getStation()));

        mockMvc.perform(get("/fire")
                        .param("address", person1.getAddress())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(2)));
    }

    @Test
    public void gtePersonAndFireStationByUnknownAddressTestFail () throws Exception {
        Mockito.when(personRepository.findByAddress(person1.getAddress())).thenReturn(new ArrayList<>());


        mockMvc.perform(get("/fire")
                        .param("address", person1.getAddress())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(0)));
    }

    @Test
    public void getHouseHoldFloodAlertTestSuccess () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()))).thenReturn(fireStation1);

        Mockito.when(houseHoldRepository.findByAddress(fireStation1.getAddresses().get(0))).thenReturn(List.of(person1));

        mockMvc.perform(get("/flood/stations")
                        .param("stations", String.valueOf(fireStation1.getStation()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(2)));
    }

    @Test
    public void getHouseHoldFloodAlertWithUnknownStationTestFail () throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()))).thenReturn(null);

        mockMvc.perform(get("/flood/stations")
                        .param("stations", String.valueOf(fireStation1.getStation()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(0)));
    }

    @Test
    public void getPersonInfoTestSuccess () throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("firstName", person1.getFirstName());
        requestParams.add("lastName", person1.getLastName());

        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(person1);

        mockMvc.perform(get("/personInfo")
                        .params(requestParams)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getUnknownPersonInfoTestFail () throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("firstName", person1.getFirstName());
        requestParams.add("lastName", person1.getLastName());

        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        mockMvc.perform(get("/personInfo")
                        .params(requestParams)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getCommunityEmailTestSuccess () throws Exception {
        Mockito.when(personRepository.findEmailsByCity(person1.getCity())).thenReturn(List.of(person1.getEmail()));

        mockMvc.perform(get("/communityEmail")
                        .param("city", person1.getCity())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem(person1.getEmail())));
    }

    @Test
    public void getCommunityEmailTestFail () throws Exception {
        Mockito.when(personRepository.findEmailsByCity(person1.getCity())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/communityEmail")
                        .param("city", person1.getCity())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
