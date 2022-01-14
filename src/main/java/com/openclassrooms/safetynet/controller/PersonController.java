package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exceptions.NotFoundException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.HelperRepository;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/person")
    public MappingJacksonValue personsList() {
        List<Person> personList = personRepository.findAll();
        return HelperRepository.getFilter("personFilter", personList, "id", "");
    }

    @GetMapping("/person/{firstName}/{lastName}")
    public MappingJacksonValue getPerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {

         Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);

         if(person == null) {
             logger.error("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
             throw new NotFoundException("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
         }

        logger.info("Informations sur la personne avec lne nom est le prénom: "+ firstName + " " + lastName);
         return HelperRepository.getNoFilter("personFilter", person);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson (@RequestBody Person person) {
        Person addedPerson = personRepository.save(person);

        if(Objects.isNull(addedPerson)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{/firstName/lastName}")
                .buildAndExpand(addedPerson.getFirstName(), addedPerson.getLastName())
                .toUri();

        logger.info("La personne a été crée avec succés!");

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/person/{firstName}/{lastName}")
    public MappingJacksonValue updatePerson (@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @RequestBody Person person) {
        Person personToUpdate = personRepository.findByFirstNameAndLastName(firstName, lastName);

        if(personToUpdate == null) {
            logger.error("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
            throw new NotFoundException("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
        }
        return HelperRepository.getNoFilter("personFilter", personRepository.update(firstName, lastName, person));
    }

    @DeleteMapping("/person/{firstName}/{lastName}")
    public void deletePerson (@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);

        if(person == null) {
            logger.error("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
            throw new NotFoundException("La personne avec le prénom et le nom: " + firstName + " " +lastName + " est introuvable!");
        }

        personRepository.delete(firstName, lastName);
    }

}
