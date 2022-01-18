package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.exceptions.NotFoundException;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

@RestController
public class FireStationController {
    @Autowired
    FireStationRepository fireStationRepository;

    private final static Logger logger = LoggerFactory.getLogger(FireStationController.class);

    @GetMapping("/firestation")
    public Map<Integer, FireStation> fireStationsList() {
        return fireStationRepository.findAll();
    }

    @GetMapping("/firestation/{id}")
    public FireStation getFireStation(@PathVariable("id") int id) {

        FireStation fireStation = fireStationRepository.findById(id);

        if(fireStation == null) {
            logger.error("La caserne de pompiers avec l'id: " + id + " est introuvable!");
            throw new NotFoundException("La caserne avec l'id': " + id + " est introuvable!");
        }

        logger.info("Informations sur la caserne de pompiers avec l'id': "+ id);
        return fireStation;
    }

    @PostMapping("/firestation")
    public ResponseEntity<Person> createPerson (@RequestBody FireStation fireStation) {
        FireStation addedFireStation = fireStationRepository.save(fireStation);

        if(Objects.isNull(addedFireStation)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{/station}")
                .buildAndExpand(addedFireStation.getStation())
                .toUri();

        logger.info("La caserne de pompiers a été crée avec succés!");

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/firestation/{id}")
    public FireStation updateFireStation (@PathVariable("id") int id, @RequestBody FireStation fireStation) {
        FireStation fireStationToUpdate = fireStationRepository.findById(id);

        if(fireStationToUpdate == null) {
            logger.error("La caserne de pompiers avec l'id': " + id + " est introuvable!");
            throw new NotFoundException("La caserne de pompiers avec l'id': " + id + " est introuvable!");
        }
        return fireStationRepository.update(id, fireStation);
    }

    @DeleteMapping("/firestation/{id}")
    public void deletePerson (@PathVariable("id") int id) {
        FireStation fireStation = fireStationRepository.findById(id);

        if(fireStation == null) {
            logger.error("La caserne de pompiers avec l'id': " + id + " est introuvable!");
            throw new NotFoundException("La caserne de pompiers avec l'id': " + id + " est introuvable!");
        }

        fireStationRepository.delete(id);
    }
}
