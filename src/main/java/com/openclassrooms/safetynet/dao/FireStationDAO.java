package com.openclassrooms.safetynet.dao;

import com.openclassrooms.safetynet.model.FireStation;
import java.util.Map;

public interface FireStationDAO {
    Map<Integer, FireStation> findAll ();

    FireStation findById(Integer id);

    Integer findByAddress (String address);

    FireStation save(FireStation fireStation);

    FireStation update(Integer id, FireStation fireStation);

    void delete(Integer id);
}
