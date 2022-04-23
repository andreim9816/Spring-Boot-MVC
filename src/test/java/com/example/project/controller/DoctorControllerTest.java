package com.example.project.controller;

import com.example.project.model.Doctor;
import com.example.project.model.security.User;
import com.example.project.service.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for {@link com.example.project.controller.DoctorController}
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DoctorServiceImpl doctorService;

    // get all

    @Test
    @WithAnonymousUser
    public void showDoctors_anonymous_success() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void showDoctors_admin_success() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void showDoctors_doctor_success() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }


    // get by id


    @Test
    @WithAnonymousUser
    public void showDoctor_anonymous_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_info"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void showDoctor_admin_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_info"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void showDoctor_doctor_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_info"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    // edit doctor (GET)


    @Test
    @WithAnonymousUser
    public void editDoctor_anonymous_fail() throws Exception {
        mockMvc.perform(get("/doctors/{id}/edit", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void editDoctor_admin_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}/edit", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_form"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void editDoctor_doctor_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}/edit", "2"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("access_denied"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void editDoctor_doctor_fail() throws Exception {
        mockMvc.perform(get("/doctors/{id}/edit", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors/my-profile"));
    }

    // edit doctor (POST)

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void editDoctor_POST_doctor_fail() throws Exception {

        Doctor doctor = doctorService.getById(1L);
        User user = doctor.getUser();

        doctor.setLastName("1234567");
        doctor.setFirstName("98765");
        user.setUsername("");

        mockMvc.perform(post("/doctors")
                        .flashAttr("doctor", doctor)
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors/my-profile"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void editDoctor_POST_doctor_success() throws Exception {

        Doctor doctor = doctorService.getById(1L);
        User user = doctor.getUser();

        doctor.setLastName("Mano");
        doctor.setFirstName("Andrei");
        user.setEmail("abc@email.com");

        mockMvc.perform(post("/doctors")
                        .flashAttr("doctor", doctor)
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors/my-profile"));

        doctor = doctorService.getById(1L);
        assertEquals("Mano", doctor.getLastName());
        assertEquals("Andrei", doctor.getFirstName());
        assertEquals("abc@email.com", doctor.getUser().getEmail());
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void editDoctor_POST_admin_fail() throws Exception {

        Doctor doctor = doctorService.getById(1L);
        User user = doctor.getUser();

        doctor.setLastName("1234567");
        doctor.setFirstName("98765");
        user.setUsername("");

        mockMvc.perform(post("/doctors")
                        .flashAttr("doctor", doctor)
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors/1/edit"));
    }

    // my-profile

    @Test
    @WithAnonymousUser
    public void myProfileDoctor_anonymous_fail() throws Exception {
        mockMvc.perform(get("/doctors/my-profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void myProfileDoctor_admin_fail() throws Exception {
        mockMvc.perform(get("/doctors/my-profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void myProfileDoctor_doctor_success() throws Exception {
        mockMvc.perform(get("/doctors/my-profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_form"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    //delete

    @Test
    @WithMockUser(username = "admin_1", password = "123456", roles = "ADMIN")
    public void deleteDoctor_admin_success() throws Exception {
        mockMvc.perform(get("/doctors/{id}/delete", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));
    }

    @Test
    @WithMockUser(username = "doctor_1", password = "123456", roles = "DOCTOR")
    public void deleteDoctor_doctor_fail() throws Exception {
        mockMvc.perform(get("/doctors/{id}/delete", "1"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access-denied"));
    }

    @Test
    @WithAnonymousUser
    public void deleteDoctor_anonymous_fail() throws Exception {
        mockMvc.perform(get("/doctors/{id}/delete", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
