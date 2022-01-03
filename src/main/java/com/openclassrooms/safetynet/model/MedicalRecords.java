package com.openclassrooms.safetynet.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MedicalRecords {
    private String birthDate;
    private List<String> allergies;
    private List<String> medications;
}
