package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.controller.FireStationController;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
public class fireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    FireStation fireStation1 = new FireStation("1", Arrays.asList("03 Rue Des 24 Arpents", "Rue Rivoli"));
    FireStation fireStation2 = new FireStation("2", Arrays.asList("Place de Clichy", "Bv Victor Bordier"));

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetFireStations() throws Exception {
        Mockito.when(fireStationRepository.findAll()).thenReturn(Map.of(Integer.parseInt(fireStation1.getStation()), fireStation1, Integer.parseInt(fireStation2.getStation()), fireStation2));

        mockMvc.perform(get("/firestation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetFireStationByStationNumberSuccess() throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(fireStation1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/firestation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.station", is("1")));
    }

    @Test
    public void testGetFireStationByStationNumberFail() throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/firestation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatFireStationSuccess () throws  Exception {
        FireStation fireStation = new FireStation("6", Arrays.asList("03 Rue Des 24 Arpents", "Rue Rivoli"));
        Mockito.when(fireStationRepository.save(fireStation)).thenReturn(fireStation);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(fireStation));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreatFireStationFail () throws  Exception {
        FireStation fireStation = new FireStation("6", Arrays.asList("03 Rue Des 24 Arpents", "Rue Rivoli"));
        Mockito.when(fireStationRepository.save(fireStation)).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(fireStation));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateFireStationSuccess() throws Exception {
        Map fireStations = Map.of(Integer.parseInt(fireStation1.getStation()), fireStation1, Integer.parseInt(fireStation2.getStation()), fireStation2);

        FireStation updatedFireStation = new FireStation("1", Arrays.asList("03 Rue Des 24 Arpents", "Rue Rivoli"));

        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn((FireStation) fireStations.get(Integer.parseInt(fireStation1.getStation())));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/firestation/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedFireStation));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFireStationByNullIdFail() throws Exception {
        FireStation updatedFireStation = new FireStation("1", Arrays.asList("03 Rue Des 24 Arpents", "Rue Rivoli"));

        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/firestation/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedFireStation));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFireStationByIdSuccess() throws Exception {

        Map fireStations = Map.of(Integer.parseInt(fireStation1.getStation()), fireStation1, Integer.parseInt(fireStation2.getStation()), fireStation2);

        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn((FireStation) fireStations.get(Integer.parseInt(fireStation1.getStation())));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/firestation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStationByNullIdFail() throws Exception {
        Mockito.when(fireStationRepository.findById(Integer.parseInt(fireStation1.getStation()))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/firestation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
