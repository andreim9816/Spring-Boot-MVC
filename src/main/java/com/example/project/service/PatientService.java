package com.example.project.service;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.model.Patient;
import com.example.project.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
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

    public Boolean checkIfPatientExists(Long patientId) {
        return patientRepository.findById(patientId).isPresent();
    }

    public Boolean checkIfCnpExists(String cnp) {
        return patientRepository.getByCnp(cnp) != null;
    }

    public List<Consult> getConsultsForPatient(Long patientId) {
        Patient patient = getPatientById(patientId);
        return getConsultsForPatient(patient);
    }

    private List<Consult> getConsultsForPatient(Patient patient) {
        return patient.getConsults();
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

//    public Patient updatePatient(ReqPatientUpdateDto reqPatientDto, Patient patient) {
//
//        /* Check for uniqueness address at DB level */
//        if (!Objects.equals(reqPatientDto.getAddressId(), patient.getAddress().getId())
//                && Boolean.TRUE.equals(addressService.checkIfAddressIsTakenByPatient(reqPatientDto.getAddressId()))) {
//            throw new CustomException(String.format("Address with id %s already taken!", reqPatientDto.getAddressId()));
//        }
//
//        /* Check for uniqueness of CNP */
//        if (!Objects.equals(reqPatientDto.getCnp(), patient.getCnp())
//                && Boolean.TRUE.equals(checkIfCnpExists(reqPatientDto.getCnp()))) {
//            throw new CustomException(String.format("CNP %s already taken!", reqPatientDto.getCnp()));
//        }
//
//        Patient updatedPatient = patientMapper.update(reqPatientDto, patient);
//
//        return savePatient(updatedPatient);
//    }

    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
