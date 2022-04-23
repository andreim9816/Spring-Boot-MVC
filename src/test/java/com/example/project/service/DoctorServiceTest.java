package com.example.project.service;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
/**
 * Test for {@link com.example.project.service.DoctorServiceImpl}
 */
public class DoctorServiceTest {

    @Autowired
    private DoctorServiceImpl doctorService;

    @Test
    public void getAll() {
        List<Doctor> doctors = doctorService.getAllDoctors();

        assertEquals(3, doctors.size());
        assertEquals("Firastrau", doctors.get(0).getLastName());
        assertEquals("Mocanu", doctors.get(1).getLastName());
        assertEquals("Asmarandei", doctors.get(2).getLastName());
    }

    @Test
    public void getById() {
        Doctor doctor = doctorService.getById(1L);
        assertNotNull(doctor);
        assertEquals("Firastrau", doctor.getLastName());
        assertEquals(1L, doctor.getDepartment().getId());
    }

    @Test
    public void getAllConsultsForDoctor() {
        List<Consult> consults = doctorService.getAllConsultsForDoctor(2L);
        assertEquals(2, consults.size());
        assertEquals(2L, consults.get(0).getId());
        assertEquals(3L, consults.get(1).getId());
    }

    @Test
    public void saveDoctor() {
        Doctor doctor = new Doctor();
        doctor.setLastName("Nume");
        doctor.setFirstName("Prenume");

        doctorService.saveDoctor(doctor);
        List<Doctor> doctors = doctorService.getAllDoctors();
        Doctor lastDoctor = doctors.get(doctors.size() - 1);

        assertEquals("Nume", lastDoctor.getLastName());
        assertEquals("Prenume", lastDoctor.getFirstName());
    }

    @Test
    public void removeDoctor() {
        doctorService.deleteDoctorById(3L);
        assertThrows(EntityNotFoundException.class, () -> doctorService.getById(3L));
    }
}
