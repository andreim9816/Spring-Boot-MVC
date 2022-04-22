package com.example.project.repository;

import com.example.project.model.Medication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("h2")
//@Rollback(false)
public class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    public void addMedication() {
        Medication medication = new Medication();
        medication.setQuantity(100);
        medication.setName("Medicament");

        medicationRepository.save(medication);
    }

    @Test
    public void findByNameAndQuantity() {
        Optional<Medication> medication = medicationRepository.findMedicationByNameAndQuantity("Paracetamol", 400);
        assertTrue(medication.isPresent());
    }

    @Test
    public void findByNameAndQuantity_notFound() {
        Optional<Medication> medication = medicationRepository.findMedicationByNameAndQuantity("AAAAA", 500);
        assertTrue(medication.isEmpty());
    }

    @Test
    public void findByIdIsIn() {
        List<Medication> medication = medicationRepository.findByIdIsIn(Arrays.asList(1L, 2L));
        assertFalse(medication.isEmpty());
    }

    @Test
    public void findAll() {
        List<Medication> medications = medicationRepository.findAll();
        assertEquals(8, medications.size());
    }

    @Test
    public void removeMedication() {
        medicationRepository.deleteById(1L);
        assertTrue(medicationRepository.findById(1L).isEmpty());
    }
}
