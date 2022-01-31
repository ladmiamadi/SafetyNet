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

    private final static Logger logger = LoggerFactory.getLogger(MedicalRecordsController.class);

    @GetMapping("/medicalRecord")
    public Iterable<MedicalRecords> getMedicalRecordsList() {
        return medicalRecordsRepository.findAll();
    }

    @GetMapping("/medicalRecord/{firstName}/{lastName}")
    public MedicalRecords getMedicalRecordsForPerson(@PathVariable("firstName") String firstName,
                                                     @PathVariable("lastName") String lastName) {

        MedicalRecords medicalRecords = medicalRecordsRepository.findByFirstNameAndLastName(firstName, lastName);

        if(medicalRecords == null) {
            logger.error("Les renseignements sur la personne avec le prénom et le nom: \" + firstName + \" \"+lastName + \" sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec le prénom et le nom: \" + firstName + \" \"+lastName + \" sont introuvables!");
        }

        logger.info("Renseignements sur la personne avec le prénom et le nom: "+ firstName+ " " + lastName);
        return medicalRecords;
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<Person> createMedicalRecords (@RequestBody MedicalRecords medicalRecords) {
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

    @PutMapping("/medicalRecord/{firstName}/{lastName}")
    public MedicalRecords updateMedicalRecords (@PathVariable("firstName") String firstName,
                                        @PathVariable("lastName") String lastName,
                                        @RequestBody MedicalRecords medicalRecords) {
        MedicalRecords medicalRecordsToUpdate = medicalRecordsRepository.findByFirstNameAndLastName(firstName, lastName);

        if(medicalRecordsToUpdate == null) {
            logger.error("Les renseignements sur la personne avec le prénom et le nom: " + firstName + " "+lastName + " sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec le prénom et le nom: " + firstName + " "+lastName + " sont introuvables!");
        }
        return medicalRecordsRepository.update(firstName, lastName, medicalRecords);
    }

    @DeleteMapping("/medicalRecord/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecords> deleteMedicalRecords (@PathVariable("firstName") String firstName,
                              @PathVariable("lastName") String lastName) {
        MedicalRecords medicalRecords = medicalRecordsRepository.findByFirstNameAndLastName(firstName, lastName);

        if(medicalRecords == null) {
            logger.error("Les renseignements sur la personne avec le prénom et le nom: " + firstName + " "+lastName + " sont introuvables!");
            throw new NotFoundException("Les renseignements sur la personne avec le prénom et le nom: " + firstName + " "+lastName + " sont introuvables!");
        }

        medicalRecordsRepository.delete(firstName, lastName);
        return ResponseEntity.noContent().build();
    }
}
