package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.repository.MedicalRecordsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordsController.class)
public class MedicalRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordsRepository medicalRecordsRepository;

    MedicalRecords medicalRecords1 = new MedicalRecords("Ladmia", "Madi", "18/07/1985", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
    MedicalRecords medicalRecords2 = new MedicalRecords("Bilal", "Madi", "14/02/1984", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());

    private ObjectMapper mapper = new ObjectMapper();

    private List<MedicalRecords> medicalRecordsList= Arrays.asList(medicalRecords1, medicalRecords2);

    @Test
    public void testGetMedicalRecords() throws Exception {
        Mockito.when(medicalRecordsRepository.findAll()).thenReturn(medicalRecordsList);
        mockMvc.perform(get("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetMedicalRecordsByFirstNameAndLastNameSuccess() throws Exception {
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(medicalRecordsList.get(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/medicalRecord/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.birthDate", is("18/07/1985")));
    }

    @Test
    public void testGetMedicalRecordsByFirstNameAndLastNameFail() throws Exception {
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/medicalRecord/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMedicalRecordsSuccess () throws  Exception {

        Mockito.when(medicalRecordsRepository.save(medicalRecords1)).thenReturn(medicalRecords1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(medicalRecords1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateMedicalRecordsFail () throws  Exception {
        Mockito.when(medicalRecordsRepository.save(medicalRecords1)).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(medicalRecords1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateMedicalRecordsSuccess() throws Exception {
        MedicalRecords updatedMedicalRecords = new MedicalRecords("Ladmia", "Madi", "31/07/2015", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(medicalRecords1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/medicalRecord/Ladmia/Madi")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedMedicalRecords));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMedicalRecordsFail() throws Exception {
        MedicalRecords updatedMedicalRecords = new MedicalRecords("Ladmia", "Madi", "31/07/2015", Arrays.asList("medicament1", "medicament2"), new ArrayList<>());
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/medicalRecord/Ladmia/Madi")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedMedicalRecords));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteMedicalRecordsByFirstNameAndLastNameSuccess() throws Exception {
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(medicalRecordsList.get(0));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/medicalRecord/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMedicalRecordsByFirstNameAndLastNameFail() throws Exception {
        Mockito.when(medicalRecordsRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/medicalRecord/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
