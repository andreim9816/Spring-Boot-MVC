package com.example.project.service.interfaces;

import com.example.project.model.Medication;

import java.util.List;

public interface MedicationService {

    List<Medication> getAllMedications();

    Medication getMedicationById(Long id);

    Boolean checkIfMedicationExists(Long id);

    Medication saveMedication(Medication medication);

    List<Medication> findMedicationsByIdContains(List<Long> ids);

    void deleteMedicationById(Long id);
}
