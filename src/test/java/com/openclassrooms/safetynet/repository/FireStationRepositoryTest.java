package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.FireStation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FireStationRepositoryTest {

    @Autowired
    FireStationRepository fireStationRepository;

    FireStation fireStation1 = new FireStation("7", Arrays.asList("Les Fennecs", "Rue VIvaldi"));
    FireStation fireStation2 = new FireStation("8", Arrays.asList("Place de Clichyyy"));


    @Test
    public void findAllFireStationsTest () {
        Map fireStationsList = fireStationRepository.findAll();

        assertThat(fireStationsList).isNotNull();
    }

    @Test
    public void findFireStationByIdTestSuccess () throws Exception {
        fireStationRepository.save(fireStation1);

        FireStation fireStation = fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()));

        assertThat(fireStation).isNotNull();
    }

    @Test
    public void findFireStationByUnknownIdTestFail () throws Exception {

        FireStation fireStation = fireStationRepository.findById(10);

        assertThat(fireStation).isNull();
    }

    @Test
    public void findFireStationByAddressTestSuccess () throws Exception {
        fireStationRepository.save(fireStation1);

        Integer fireStationNumber = fireStationRepository.findByAddress("Les Fennecs");

        assertThat(fireStationNumber).isEqualTo(Integer.valueOf(fireStation1.getStation()));
    }

    @Test
    public void findFireStationByAddressTestFail () throws Exception {

        Integer fireStationNumber = fireStationRepository.findByAddress("1 2 3");

        assertThat(fireStationNumber).isNull();
    }

    @Test
    public void deleteFireStationTest () {
        fireStationRepository.save(fireStation1);
        fireStationRepository.delete(Integer.valueOf(fireStation1.getStation()));

        assertThat(fireStationRepository.findById(Integer.valueOf(fireStation1.getStation()))).isNull();
    }

    @Test
    public void updateFireStationTest () {
        fireStationRepository.save(fireStation2);
        fireStation2.setAddresses(List.of("15 Rue Des Olives"));
        fireStationRepository.update(Integer.valueOf(fireStation2.getStation()), fireStation2);

        assertThat(fireStationRepository.findById(Integer.valueOf(fireStation2.getStation())).getAddresses()).isEqualTo(List.of("15 Rue Des Olives"));
    }
}
