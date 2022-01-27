package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.FireStationRepository;
import com.openclassrooms.safetynet.repository.HelperRepository;
import com.openclassrooms.safetynet.repository.HouseHoldRepository;
import com.openclassrooms.safetynet.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
public class AlertController {
    private static final Logger logger = getLogger(AlertController.class);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FireStationRepository fireStationRepository;

    @Autowired
    HouseHoldRepository houseHoldRepository;

    //Récuperer la Liste des habitant couverts par une station
    @GetMapping("/firestations")
    public MappingJacksonValue getCoveredPersonsByFireStation (@RequestParam(value = "stationNumber") Integer stationNumber) {
        List personsList = new ArrayList<>();


            FireStation fireStation = fireStationRepository.findById(stationNumber);
            if(fireStation == null) {
                logger.error("La caserne de pompiers ayant le numéro " + stationNumber + " est introuvable!");
                return HelperRepository.getFilter("personFilter", personsList, "");
            }

            List<String> addresses = new ArrayList<>(fireStation.getAddresses());

            addresses.forEach(address -> personsList.addAll(personRepository.findByAddress(address)));

            personsList.add(HelperRepository.countChildrenAndAdults(personsList));
            logger.info("Calcul l'age de l'habitant avec succées");

            return HelperRepository.getFilter("personFilter", personsList, "medicalRecords", "email", "city", "zip");
        }

    //Récuperer la liste d'enfants habitants à une adresse
    @GetMapping("/childAlert")
    public Map<String, List<Map<String, String>>> getChildrenByAddress(@RequestParam("address") String address) {
        Map<String, List<Map<String, String>>> personList = new LinkedHashMap<>();

            List<Map<String, String>> childrenList = houseHoldRepository.findChildrenByAddress(address);
            if( !childrenList.isEmpty()) {
                List<Map<String, String>> adultList = houseHoldRepository.findAdultsByAddress(address);
                personList.put("List of children", childrenList);
                logger.debug("Liste d'enfants: " + childrenList);
                personList.put("other members of the household", adultList);
                logger.debug("Liste d'adulte: " + adultList);
                logger.info("Obtention de la liste des enfants avec succées");
            } else {
                logger.info("Pas d'enfants dans le foyer ayant l'adresse " + address + " ou bien l'adresse est incorrecte!!");
                return personList;
            }

        return personList;
    }

    //Récupérer les numéros de téléphone des personnes desservis par la caserne de pompiers
    @GetMapping("/phoneALert")
    public List<String> phonesList(@RequestParam("firestation") Integer firestation) {
        List<String> phoneList = new ArrayList<>();

            FireStation fireStation = fireStationRepository.findById(firestation);

            if(fireStation == null) {
                logger.error("la station de pompiers avec le numéro "+fireStation +" n'existe pas!!");
                return phoneList;
            }

            List<String> addresses = new ArrayList<>(fireStation.getAddresses());
            addresses.forEach(address -> personRepository.findByAddress(address).forEach(person ->{
                if(!phoneList.contains(person.getPhone())) {
                    phoneList.add(person.getPhone());
                    logger.debug("Numéro de téléphone: " + person.getPhone());
                }
                    }));
            logger.info("Obtention des numéros de téléphones avec succées!");

        return phoneList;
    }

    // Récupérer la liste des habitants et la caserne qui les desservit à partir d'une adresse donnée
    @GetMapping("/fire")
    public Map getPersonsAndFireStationByAddress (@RequestParam("address") String address) {
        Map fireStationPersonsList = new LinkedHashMap<>();
        List<Map> personsList = new ArrayList<>();

            if(personRepository.findByAddress(address).isEmpty()) {
                logger.error("L'adresse "+address+ " est introuvable!!");
                return new HashMap();
            }

            for(Person person: personRepository.findByAddress(address)) {
                Map member = new LinkedHashMap<>();
                member.put("firstName", person.getFirstName());
                member.put("lastName", person.getLastName());
                member.put("age", HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate()) + " years");
                member.put("medications", person.getMedicalRecords().getMedications());
                member.put("allergies", person.getMedicalRecords().getAllergies());

                logger.debug("Membbre: " + member);
                personsList.add(member);
            }

            fireStationPersonsList.put("persons", personsList);
            fireStationPersonsList.put("covered By station ", fireStationRepository.findByAddress(address));

        return fireStationPersonsList;
    }

    //Récuperer les habitants d'une addresse desservie par une station de pompiers
    @GetMapping("/flood/stations")
    public Map<String, List> getHouseHoldForFloodALert (@RequestParam("stations") List<String> stations) throws IOException {
        List<String> addresses = new ArrayList<>();
        Map<String, List> houseHold = new HashMap<>();
        List<Map> persons = new ArrayList<>();

        if(!stations.isEmpty()) {
            for (String station : stations) {
                if(fireStationRepository.findById(Integer.valueOf(station)) != null) {
                    addresses.addAll(fireStationRepository.findById(Integer.parseInt(station)).getAddresses());
                } else {
                    logger.error("La station "+ station + " n'existe pas!!");
                }
            }
            if (addresses.isEmpty()) {
                return houseHold;
            }
        }

        for(String address: addresses) {
            houseHoldRepository.findByAddress(address).forEach(inhabitant -> {
                Map person = new LinkedHashMap<>();
                person.put("firstName", inhabitant.getFirstName());
                person.put("lastName", inhabitant.getLastName());
                person.put("phone", inhabitant.getPhone());
                person.put("age", String.valueOf(HelperRepository.calculateAge(inhabitant.getMedicalRecords().getBirthDate())));
                person.put("medications", inhabitant.getMedicalRecords().getMedications());
                person.put("allergies", inhabitant.getMedicalRecords().getAllergies());

                persons.add(person);
            });

            houseHold.put(address, persons);
        }
        return houseHold;
    }

    //Récupérer les info d'une personne
    @GetMapping("/personInfo")
    public List<Map> getPersonInfo(@RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName) {

        List<Map> personInfoList = new ArrayList<>();
        Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);

        if(person == null) {
            logger.error("La personne " + firstName+" " + lastName+" n'existe pas!!");
            return new ArrayList<>();
        }

            Map info = new LinkedHashMap<>();

            info.put("firstName", person.getFirstName());
            info.put("lastName", person.getLastName());
            info.put("address", person.getAddress());
            info.put("age", HelperRepository.calculateAge(person.getMedicalRecords().getBirthDate()) + " years");
            info.put("medications", person.getMedicalRecords().getMedications());
            info.put("allergies", person.getMedicalRecords().getAllergies());

            personInfoList.add(info);

            logger.debug("Personne: "+person );
            logger.info("Récuperation des informations sur la personne avec succées!!");

        return personInfoList;
    }

    //Récupérer les emails de tous habitants d'une ville'
    @GetMapping("/communityEmail")
    public List<String> emailsList(@RequestParam("city") String city) {
        List<String> emailsList = new ArrayList<>();

            if(personRepository.findEmailsByCity(city).isEmpty()) {
                logger.error("la cité "+ city+ " est introuvable!!");
                return emailsList;
            }

            for(String email: personRepository.findEmailsByCity(city)) {
                if(!emailsList.contains(email)) {
                    emailsList.add(email);
                }
            }
            logger.debug("Liste des email: " + emailsList);

        return emailsList;
    }
}
