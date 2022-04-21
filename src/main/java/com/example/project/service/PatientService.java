package com.example.project.service;

import com.example.project.exception.CustomException;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.model.Patient;
import com.example.project.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> EntityNotFoundException.builder()
                        .entityId(patientId)
                        .entityType("Patient")
                        .build());
    }

    public List<Consult> getConsultsForPatient(Long patientId) {
        Patient patient = getPatientById(patientId);
        return getConsultsForPatient(patient);
    }

    private List<Consult> getConsultsForPatient(Patient patient) {
        return patient.getConsults();
    }

    public Patient savePatient(Patient patient) {

        var existingPatient = patientRepository.getByCnp(patient.getCnp());
        if (existingPatient.isEmpty()) {
            return patientRepository.save(patient);
        }

        if (patient.getId() == null) {
            // save flow
            if (existingPatient.get().getCnp().equals(patient.getCnp())) {
                throw new CustomException(String.format("CNP %s already taken!", patient.getCnp()));
            }
        } else {
            // edit flow
            if (existingPatient.get().getCnp().equals(patient.getCnp()) && !existingPatient.get().getId().equals(patient.getId())) {
                throw new CustomException(String.format("CNP %s already taken!", patient.getCnp()));
            }
        }

        return patientRepository.save(patient);
    }

    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
