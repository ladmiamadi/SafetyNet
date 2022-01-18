package com.openclassrooms.safetynet.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
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
import java.util.List;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    Person person1 = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
    Person person2 = new Person("John", "Doe", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
    Person person3 = new Person("Anais", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);

    private ObjectMapper mapper = new ObjectMapper();

    private List<Person> personList = Arrays.asList(person1, person2, person3);

    @Test
    public void testGetPersons() throws Exception {
        Mockito.when(personRepository.findAll()).thenReturn(Arrays.asList(person1, person2, person3));

        mockMvc.perform(get("/person")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName" , is("Ladmia")));
    }

    @Test
    public void testGetPersonByFirstNameAndLastNameSuccess() throws Exception {
        Mockito.when(personRepository.findByFirstNameAndLastName(person1.getFirstName(), person1.getLastName())).thenReturn(person1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Ladmia")));
    }

    @Test
    public void testGetPersonByFirstNameAndLastNameFail() throws Exception {
        Mockito.when(personRepository.findByFirstNameAndLastName(person1.getFirstName(), person1.getLastName())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePersonSuccess () throws  Exception {
        Person person = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
        Mockito.when(personRepository.save(person)).thenReturn(person);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.setFilterProvider(new SimpleFilterProvider().addFilter("personFilter",
                    SimpleBeanPropertyFilter.serializeAll())).writeValueAsString(person));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreatePersonFail () throws  Exception {

        Person person = new Person("Ladmia", "Madi", "0605612151", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);

        Mockito.when(personRepository.save(person)).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/person");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    /*@Test
    public void testUpdatePersonSuccess() throws Exception {
        Person updatedPerson = new Person("Ladmia", "Madi", "06060505", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(person1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/person/Ladmia/Madi")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.setFilterProvider(new SimpleFilterProvider().addFilter("personFilter",
                        SimpleBeanPropertyFilter.serializeAll())).writeValueAsString(updatedPerson));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }*/

    @Test
    public void testUpdatePersonFail() throws Exception {
        Person updatedPerson = new Person("Ladmia", "Madi", "06060505", "ladmia.madi@gmail.com", "Rue des 24 Arpents", "Culver", "95370", null);
        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/person/Ladmia/Madi")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.setFilterProvider(new SimpleFilterProvider().addFilter("personFilter",
                        SimpleBeanPropertyFilter.serializeAll())).writeValueAsString(updatedPerson));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePersonByFirstNameAndLastNameSuccess() throws Exception {
        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(personList.get(0));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/person/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePersonByFirstNameAndLastNameFail() throws Exception {
        Mockito.when(personRepository.findByFirstNameAndLastName("Ladmia", "Madi")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/person/Ladmia/Madi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
