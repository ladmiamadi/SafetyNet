package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.FireStationDAO;
import com.openclassrooms.safetynet.model.FireStation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class FireStationRepository implements FireStationDAO {

    private static Map<Integer, FireStation> fireStationList = new HashMap<>();

    public FireStationRepository() throws IOException {
        DataFromJson dataFromJson = new DataFromJson();
        fireStationList = dataFromJson.getFireStations();
    }

    @Override
    public Map<Integer, FireStation> findAll() {
        return fireStationList;
    }

    @Override
    public FireStation findById(Integer id) {
        for (Map.Entry<Integer, FireStation> mapEntry : fireStationList.entrySet()) {
            if(Objects.equals(mapEntry.getKey(), id)) {
                return mapEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public Integer findByAddress(String address) {
        for (Map.Entry<Integer, FireStation> entry : fireStationList.entrySet()) {
            if(entry.getValue().getAddresses().contains(address)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public FireStation save(FireStation fireStation) {
        fireStationList.put(Integer.valueOf(fireStation.getStation()), fireStation);

        return fireStation;
    }

    @Override
    public FireStation update(Integer id, FireStation fireStation) {
        fireStationList.put(id, fireStation);

        return findById(id);
    }

    @Override
    public void delete(Integer id) {
        fireStationList.remove(id);
    }
}
