package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.dao.DataFromJson;
import com.openclassrooms.safetynet.dao.FireStationDAO;
import com.openclassrooms.safetynet.model.FireStation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository implements FireStationDAO {
    private DataFromJson dataFromJson = new DataFromJson();

    private static List<FireStation> fireStationList = new ArrayList<>();

    public FireStationRepository() throws IOException {
        fireStationList = dataFromJson.getFireStations();
    }

    @Override
    public List<FireStation> findAll() {
        return fireStationList;
    }

    @Override
    public FireStation findById(int id) {
        for(FireStation fireStation: fireStationList) {
            if(fireStation.getId() == id) {
                return fireStation;
            }
        }
        return null;
    }

    @Override
    public FireStation save(FireStation fireStation) {
        fireStationList.add(fireStation);

        return fireStation;
    }

    @Override
    public FireStation update(int id, FireStation fireStation) {
        fireStationList.set(id, fireStation);

        return findById(id);
    }

    @Override
    public void delete(int id) {
        fireStationList.remove(id);
    }
}
