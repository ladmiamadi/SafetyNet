package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exceptions.NotFoundPersonException;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    private static Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/person")
    public Iterable<Person> personsList() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable("id") int id) {

         Person person = personRepository.findById(id);

         if(person == null) {
             logger.error("La personne avec l'id: " + id + " est introuvable!");
             throw new NotFoundPersonException("La personne avec l'id: " + id + " est introuvable!");
         }

        logger.info("Informations sur la personne avec l'id: "+ id);
         return person;
    }

    @PostMapping("/person")
    public ResponseEntity<Person> createPerson (@RequestBody Person person) {
        Person addedPerson = personRepository.save(person);

        if(Objects.isNull(addedPerson)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{/id}")
                .buildAndExpand(addedPerson.getId())
                .toUri();

        logger.info("La personne a été crée avec succés!");

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/person/{id}")
    public Person updatePerson (@PathVariable("id") int id, @RequestBody Person person) {
        Person personToUpdate = personRepository.findById(id);

        if(personToUpdate == null) {
            logger.error("La personne avec l'id: " + id + " est introuvable!");
            throw new NotFoundPersonException("La personne avec l'id: " + id + " est introuvable!");
        }
        return personRepository.update(id, person);
    }

    @DeleteMapping("/person/{id}")
    public void deletePerson (@PathVariable("id") int id) {
        Person person = personRepository.findById(id);

        if(person == null) {
            logger.error("La personne avec l'id: " + id + " est introuvable!");
            throw new NotFoundPersonException("La personne avec l'id: " + id + " est introuvable!");
        }

        personRepository.delete(id);
    }
}
