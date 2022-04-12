package com.example.project.service;

import com.example.project.exception.CustomException;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Medication;
import com.example.project.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll().stream()
                .sorted(Comparator.comparing(Medication::getName).thenComparing(Medication::getQuantity))
                .collect(Collectors.toList());
    }

    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.builder()
                        .entityId(id)
                        .entityType("Medication")
                        .build()
                );
    }

    public Boolean checkIfMedicationExists(Long id) {
        return medicationRepository.findById(id).isPresent();
    }

    public Medication saveMedication(Medication medication) {
        checkIfMedicationAlreadyExistsByNameAndQuantity(medication.getName(), medication.getQuantity());
        return medicationRepository.save(medication);
    }


    public void deleteMedicationById(Long id) {
        medicationRepository.deleteById(id);
    }

    private void checkIfMedicationAlreadyExistsByNameAndQuantity(String name, Integer quantity) {
        if (medicationRepository.findMedicationByNameAndQuantity(name, quantity) != null) {
            throw new CustomException(String.format("Medication %s with quantity %s already exists!", name, quantity));
        }
    }
}
