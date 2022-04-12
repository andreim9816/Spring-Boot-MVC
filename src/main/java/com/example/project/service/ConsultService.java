package com.example.project.service;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.repository.ConsultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultService {

    private final ConsultRepository consultRepository;

    public List<Consult> getAllConsults() {
        return consultRepository.findAll();
    }

    public Consult getConsultById(Long id) {
        return consultRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.builder()
                        .entityId(id)
                        .entityType("Consult")
                        .build()
                );
    }

    public Boolean checkIfConsultExists(Long id) {
        return consultRepository.findById(id).isPresent();
    }

    public List<Consult> getAllConsultsForDoctorAndPatient(Long doctorId, Long patientId) {
        return consultRepository.getConsultsByDoctorIdAndPatientId(doctorId, patientId);
    }

    public Consult saveConsult(Consult consult) {
        consult.setDate(new Date());
        return consultRepository.save(consult);
    }

    public void deleteConsultById(Long id) {
        consultRepository.deleteById(id);
    }
}
