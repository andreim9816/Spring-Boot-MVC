package com.example.project.service;

import com.example.project.exception.CustomException;
import com.example.project.model.Patient;
import com.example.project.repository.PatientRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    private static Patient patient1, patient2, patient3;

    @BeforeAll
    static void createPatientObjects() {
        patient1 = new Patient();
        patient2 = new Patient();
        patient3 = new Patient();

        patient1.setId(1L);
        patient1.setCnp("1981127282920");
        patient1.setLastName("Nume");
        patient1.setFirstName("Prenume");

        patient2.setId(2L);
        patient2.setCnp("1981100082920");
        patient2.setLastName("Eminescu");
        patient2.setFirstName("Mihai");

        patient3.setId(3L);
        patient3.setCnp("9991100082920");
        patient3.setLastName("Ciubotaru");
        patient3.setFirstName("Otilia");
    }

    @Test
    public void findAllPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2, patient3));
        List<Patient> result = patientService.getAllPatients();

        assertEquals(3, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void findById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.ofNullable(patient1));
        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    public void savePatient_cnpNotFound() {
        when(patientRepository.getByCnp("1981127282920")).thenReturn(Optional.empty());
        when(patientRepository.save(patient1)).thenReturn(patient1);

        Patient result = patientService.savePatient(patient1);
        assertNotNull(result);
        assertEquals("1981127282920", result.getCnp());

        verify(patientRepository, times(0)).findById(1L);
        verify(patientRepository, times(1)).save(patient1);
    }

    @Test
    public void savePatient_cnpFound_Edit() {
        when(patientRepository.getByCnp("1981127282920")).thenReturn(Optional.ofNullable(patient1));
        when(patientRepository.save(patient1)).thenReturn(patient1);

        Patient result = patientService.savePatient(patient1);
        assertNotNull(result);
        assertEquals("1981127282920", result.getCnp());

        verify(patientRepository, times(0)).findById(1L);
        verify(patientRepository, times(1)).save(patient1);
    }

    @Test
    public void savePatient_cnpFound_cnpAlreadyTaken() {
        patient1.setId(null);
        when(patientRepository.getByCnp("1981127282920")).thenReturn(Optional.ofNullable(patient1));

        try {
            patientService.savePatient(patient1);
        }catch (CustomException e) {
            assertEquals("CNP 1981127282920 already taken!", e.getMessage());
        }

        verify(patientRepository, times(0)).findById(1L);
        verify(patientRepository, times(0)).save(patient1);
    }
}
