package com.example.project;

import com.example.project.model.Address;
import com.example.project.model.Department;
import com.example.project.model.Patient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("h2")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CascadeTypeTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void oneToOne_save() {
        Patient patient = new Patient();
        patient.setCnp("1981127272727");
        patient.setConsults(new ArrayList<>());
        patient.setFirstName("Andrei");
        patient.setLastName("Mano");

        Address address = new Address();
        address.setCity("Roman");
        address.setNo(2);
        address.setStreet("Strada");

        patient.setAddress(address);

        entityManager.persist(patient);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void oneToOne_update() {
        Patient patient = entityManager.find(Patient.class, 1L);

        Address address = patient.getAddress();
        address.setStreet("Independentei");
        address.setNo(100);
        address.setCity("Timisoara");

        patient.setAddress(address);

        entityManager.merge(patient);
        entityManager.flush();

        patient = entityManager.find(Patient.class, 1L);
        assertEquals("Independentei", patient.getAddress().getStreet());
        assertEquals("Timisoara", patient.getAddress().getCity());
        assertEquals(100, patient.getAddress().getNo());
        assertNotNull(patient.getAddress().getId());
    }

    @Test
    public void oneToOne_remove() {
        Patient patient = entityManager.find(Patient.class, 1L);
        Long addressId = patient.getAddress().getId();

        entityManager.remove(patient);
        Address address = entityManager.find(Address.class, addressId);
        assertNull(address);
    }

    @Test
    public void oneToMany_save() {
        Department department = entityManager.find(Department.class, 1L);

        Patient patient = new Patient();
        patient.setFirstName("Andrei");
        patient.setLastName("Mano");
        patient.setCnp("1981128270000");

        department.getPatients().add(patient);

        entityManager.persist(department);

        assertNotNull(department.getPatients().get(department.getPatients().size() - 1).getId());
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void oneToMany_delete() {
        Department department = entityManager.find(Department.class, 1L);
        List<Long> patientIds = department.getPatients().stream().map(Patient::getId).collect(Collectors.toList());
        if (patientIds.size() == 0) {
            fail();
        }
        entityManager.remove(department);
        entityManager.flush();

        patientIds.forEach(id -> assertNull(entityManager.find(Patient.class, id)));
    }
}
