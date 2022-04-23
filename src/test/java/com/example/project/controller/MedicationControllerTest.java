package com.example.project.controller;

import com.example.project.model.Medication;
import com.example.project.service.MedicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for {@link com.example.project.controller.MedicationController}
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
public class MedicationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MedicationServiceImpl medicationService;

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    @DisplayName("Test that validation form has input errors")
    void addNewMedicationFailsValidation() throws Exception {

        Medication medication = new Medication();
        medication.setName("");
        medication.setQuantity(-2);
        mockMvc.perform(post("/medications").flashAttr("medication", medication))
                .andExpect(model().attributeHasFieldErrors("medication", "quantity"))
                .andExpect(model().attributeHasFieldErrors("medication", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("medication_form"));
    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    @DisplayName("Test that validation fails when already existing medication - quantity in database")
    void addNewMedication_Fails() throws Exception {

        Medication medication = new Medication();
        medication.setName("Controloc");
        medication.setQuantity(400);

        mockMvc.perform(post("/medications").flashAttr("medication", medication))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medications/new"))
                .andExpect(redirectedUrl("/medications/new"));

    }

    @Test
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    @DisplayName("Test that validation fails when already existing medication - quantity in database")
    void editMedication_Fails() throws Exception {

        Medication medication = medicationService.getMedicationById(1L);

        medication.setName("Vitamina C");
        medication.setQuantity(1000);

        mockMvc.perform(post("/medications").flashAttr("medication", medication))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/medications/1/edit"))
                .andExpect(redirectedUrl("/medications/1/edit"));

    }
}
