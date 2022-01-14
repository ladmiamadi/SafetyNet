package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exceptions.NotFoundException;
import com.openclassrooms.safetynet.model.MedicalRecords;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.MedicalRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.Objects;

@RestController
public class MedicalRecordsController {

    @Autowired
    MedicalRecordsRepository medicalRecordsRepository;

    private final static Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/medicalRecord")
    public Iterable<MedicalRecords> MedicalRecordsList() {
        return medicalRecordsRepository.findAll();
    }

    @GetMapping("/medicalRecord/{id}")
    public MedicalRecords getPerson(@PathVariable("id") int id) {

        MedicalRecords medicalRecords = medicalRecordsRepository.findById(id);

        if(medicalRecords == null) {
            logger.error("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
        }

        logger.info("Renseignements sur la personne avec l'id: "+ id);
        return medicalRecords;
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<Person> createPerson (@RequestBody MedicalRecords medicalRecords) {
        MedicalRecords addedMedicalRecords = medicalRecordsRepository.save(medicalRecords);

        if(Objects.isNull(addedMedicalRecords)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{/id}")
                .buildAndExpand(addedMedicalRecords.getFirstName())
                .toUri();

        logger.info("Les rensignements sur la personne ont été crées avec succés!");

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/medicalRecord/{id}")
    public MedicalRecords updatePerson (@PathVariable("id") int id, @RequestBody MedicalRecords medicalRecords) {
        MedicalRecords medicalRecordsToUpdate = medicalRecordsRepository.findById(id);

        if(medicalRecordsToUpdate == null) {
            logger.error("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
        }
        return medicalRecordsRepository.update(id, medicalRecords);
    }

    @DeleteMapping("/medicalRecord/{id}")
    public void deletePerson (@PathVariable("id") int id) {
        MedicalRecords medicalRecords = medicalRecordsRepository.findById(id);

        if(medicalRecords == null) {
            logger.error("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec l'id: " + id + " sont introuvables!");
        }

        medicalRecordsRepository.delete(id);
    }
}
