package com.example.project.service.interfaces;

import com.example.project.model.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAllPatients();

    Patient getPatientById(Long patientId);

    Patient savePatient(Patient patient);

    void deletePatientById(Long patientId);
}
