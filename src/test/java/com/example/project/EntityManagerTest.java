package com.example.project;

import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import com.example.project.model.Medication;
import com.example.project.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
@ActiveProfiles("h2")
@Slf4j
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    public void findDoctor() {
        Doctor doctor = entityManager.find(Doctor.class, 1L);
        assertNotNull(doctor);
        assertEquals("Victor", doctor.getFirstName());
        assertEquals("Firastrau", doctor.getLastName());
        assertEquals(1L, doctor.getDepartment().getId());
    }

    @Test
    @Order(2)
    public void updateDoctor() {
        Doctor doctor = entityManager.find(Doctor.class, 1L);
        doctor.setLastName("Mocanu");
        entityManager.persist(doctor);
        entityManager.flush();
    }

    @Test
    @Order(3)
    public void findConsult() {
        Consult consult = entityManager.find(Consult.class, 1L);
        assertNotNull(consult);
        assertEquals(1L, consult.getDoctor().getId());
        assertEquals(1L, consult.getPatient().getId());
        assertEquals("-", consult.getComment());
        assertEquals("Durere de cap, febra", consult.getSymptoms());
    }

    @Test
    @Order(4)
    public void updateConsult() {
        Consult consult = entityManager.find(Consult.class, 1L);
        consult.setDate(new Date());
        consult.setComment("new comment");
        consult.setSymptoms("new symptoms");
        consult.setDiagnose("new diagnose");
        consult.setMedications(new ArrayList<>());

        Patient newPatient = entityManager.find(Patient.class, 3L);
        consult.setPatient(newPatient);

        entityManager.persist(consult);
        entityManager.flush();
    }

    @Test
    @Order(5)
    public void findMedication() {
        Medication medication = entityManager.find(Medication.class, 1L);
        assertNotNull(medication);
        assertEquals("Controloc", medication.getName());
        assertEquals(400, medication.getQuantity());
    }

    @Test
    @Order(6)
    public void updateMedication() {
        Medication medication = entityManager.find(Medication.class, 1L);
        medication.setName("Parasinus");
        medication.setQuantity(500);
        entityManager.persist(medication);
        entityManager.flush();
    }
}
