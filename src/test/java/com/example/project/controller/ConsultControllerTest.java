package com.example.project.controller;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import com.example.project.model.Medication;
import com.example.project.model.Patient;
import com.example.project.service.ConsultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for {@link ConsultController}
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
public class ConsultControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ConsultServiceImpl consultService;

    @MockBean
    Model model;

    private Doctor doctor1;
    private Patient patient1;
    private Consult consult1, consult2, consult3;
    private Medication medication;

    @BeforeEach
    public void setUp() {
        doctor1 = new Doctor();
        doctor1.setLastName("Nume");
        doctor1.setFirstName("Prenume");

        patient1 = new Patient();
        patient1.setLastName("Numee");
        patient1.setFirstName("Prenumee");

        medication = new Medication();
        medication.setName("Medicament");
        medication.setQuantity(100);

        consult1 = new Consult();
        consult1.setComment("Comment 1");
        consult1.setDiagnose("Diagnose 1");
        consult1.setSymptoms("Symp 1");
        consult1.setDate(new Date());
        consult1.setPatient(patient1);
        consult1.setDoctor(doctor1);
        consult1.setMedications(List.of(medication));

        consult2 = new Consult();
        consult2.setComment("Comment 2");
        consult2.setDiagnose("Diagnose 2");
        consult2.setSymptoms("Symp 2");
        consult2.setDate(new Date());
        consult2.setPatient(patient1);
        consult2.setDoctor(doctor1);
        consult2.setMedications(List.of(medication));


        consult3 = new Consult();
        consult3.setComment("Comment 3");
        consult3.setDiagnose("Diagnose 3");
        consult3.setSymptoms("Symp 3");
        consult3.setDate(new Date());
        consult3.setPatient(patient1);
        consult3.setDoctor(doctor1);
        consult3.setMedications(List.of(medication));

    }

    @Test
    @WithAnonymousUser
    public void accessConsults_Fails() throws Exception {
        mockMvc.perform(get("/consults"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void accessConsults_Admin_Success() throws Exception {
        var consults = new PageImpl<>(List.of(consult1, consult2));
        when(consultService.getAllConsults(any())).thenReturn(consults);
        mockMvc.perform(get("/consults"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("consults", consults));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void accessConsults_Doctor_Success() throws Exception {
        var consults = new PageImpl<>(List.of(consult1, consult2));
        when(consultService.getAllConsults(any())).thenReturn(consults);
        mockMvc.perform(get("/consults"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("consults", consults));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void accessConsult_Doctor_Success() throws Exception {
        when(consultService.getConsultById(1L)).thenReturn(consult1);
        mockMvc.perform(get("/consults/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("consult", consult1))
                .andExpect(model().attribute("doctorName", "Nume Prenume"))
                .andExpect(model().attribute("patientName", "Numee Prenumee"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void accessConsult_Admin_Fails() throws Exception {
        when(consultService.getConsultById(1L)).thenThrow(new EntityNotFoundException("Consult", 1L));
        mockMvc.perform(get("/consults/{id}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error_not_found"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void accessNewConsult_Admin_Success() throws Exception {
        mockMvc.perform(get("/consults/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("consult_form"));
    }

    @Test
    @WithAnonymousUser
    public void accessNewConsult_Anonymous_Success() throws Exception {
        mockMvc.perform(get("/consults/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void saveNewConsult_Admin_Success() throws Exception {
        when(consultService.saveConsult(any())).thenReturn(consult1);
        mockMvc.perform(post("/consults")
                        .flashAttr("consult", consult1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/consults/new"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    void accessMovieDeleteAccessDenied() throws Exception {
        doNothing().when(consultService).deleteConsultById(1L);
        mockMvc.perform(get("/consults/1/delete"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access-denied"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    void accessMovieDeleteSuccess() throws Exception {
        doNothing().when(consultService).deleteConsultById(1L);
        mockMvc.perform(get("/consults/1/delete"))
                .andExpect(redirectedUrl("/consults"));
    }

}
