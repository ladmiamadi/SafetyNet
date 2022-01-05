package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.FireStation;

import java.util.List;

public interface FireStationDAO {
    List<FireStation> findAll ();

    FireStation findById(int id);

    FireStation save(FireStation fireStation);

    FireStation update(int id, FireStation fireStation);

    void delete(int id);
}
