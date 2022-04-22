package com.example.project.repository;

import com.example.project.model.Consult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("h2")
public class ConsultRepositoryTest {

    @Autowired
    private ConsultRepository consultRepository;

    @Test
    public void getAll() {
        assertTrue(consultRepository.findAll().size() > 0);
    }

    @Test
    public void getConsultsByDoctorId() {
        List<Consult> consults = consultRepository.getConsultsByDoctorId(2L);
        assertEquals(2, consults.size());
    }

    @Test
    public void getConsultsByDoctorIdPaginated() {
        Page<Consult> consults = consultRepository.getConsultsByDoctorId(2L, PageRequest.of(0, 10, Sort.by("date")));
        assertEquals(2, consults.getTotalElements());
        assertTrue(consults.getContent().get(0).getDate().before(consults.getContent().get(1).getDate()));
    }

    @Test
    public void getConsultById() {
        assertTrue(consultRepository.findById(1L).isPresent());
    }

    @Test
    public void removeConsult() {
        consultRepository.deleteById(1L);
        assertTrue(consultRepository.findById(1L).isEmpty());
    }
}
