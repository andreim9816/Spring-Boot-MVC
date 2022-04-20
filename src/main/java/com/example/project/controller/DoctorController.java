package com.example.project.controller;

import com.example.project.exception.NotUniqueEmailException;
import com.example.project.exception.NotUniqueUsernameException;
import com.example.project.model.Doctor;
import com.example.project.model.security.User;
import com.example.project.service.DepartmentService;
import com.example.project.service.DoctorService;
import com.example.project.service.security.AuthorityService;
import com.example.project.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;

import static com.example.project.configuration.SecurityConfig.ROLE_DOCTOR;
import static com.example.project.controller.DepartmentController.BINDING_RESULT_PATH;
import static com.example.project.controller.DepartmentController.REDIRECT;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final static String ALL_DOCTORS = "doctors";
    private final static String VIEW_DOCTOR = "doctor_info";
    private final static String ADD_EDIT_DOCTOR = "doctor_form";

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final AuthorityService authorityService;

    @GetMapping
    public String getAll(Model model) {
        var doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return ALL_DOCTORS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") String doctorId, Model model) {
        var doctor = doctorService.getById(Long.valueOf(doctorId));
        model.addAttribute("doctor", doctor);
        return VIEW_DOCTOR;
    }

//    @GetMapping("/new")
//    public String addDoctor(Model model) {
//        if (!model.containsAttribute("doctor")) {
//            model.addAttribute("doctor", new Doctor());
//        }
//        model.addAttribute("departmentAll", departmentService.getAllDepartments());
//
//        return ADD_EDIT_DOCTOR;
//    }

    @GetMapping("/my-profile")
    public String getMyProfile(Model model) {
        // here is the view accessible only to the doctors where they can see info about themselves and edit data
        var user = userService.getCurrentUser();
        var doctor = user.getDoctor();

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", user);
        } else {
            user = (User) model.getAttribute("user");
            user.setPassword("");
        }
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", doctor);
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());
        return "doctors_form_2";
    }

    @GetMapping("/{id}/edit")
    public String editDoctor(@PathVariable("id") String doctorId, Model model) {
        if (userService.isDoctor()) {
            if (!userService.checkIfCurrentUserIsSameDoctor(Long.valueOf(doctorId))) {
                return "access_denied";
            } else {
                return REDIRECT + ALL_DOCTORS + "/my-profile";
            }
        }
        // is admin
        var doctor = doctorService.getById(Long.valueOf(doctorId));
        var user = doctor.getUser();

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", user);
        } else {
            user = (User) model.getAttribute("user");
            user.setPassword("");
        }
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", doctor);
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        //todo change view-ul de add-edit-doctor sa contina fieldurile bune
        // todo
        return "doctors_form_2";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                               @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResultDoctor,
                               RedirectAttributes attr) {
        if (bindingResultUser.hasErrors() || bindingResultDoctor.hasErrors()) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);

            return REDIRECT + ALL_DOCTORS + "/" + doctor.getId() + "/edit";
        }

        user.setAuthorities(Set.of(authorityService.getByRole(ROLE_DOCTOR)));
        user.setDoctor(doctor);
        doctor.setUser(user);

        try {
            userService.saveOrUpdateUser(user);
        } catch (NotUniqueEmailException e) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);
            attr.addFlashAttribute("error_email", e.getMessage());
            return REDIRECT + ALL_DOCTORS + "/" + doctor.getId() + "/edit";
        } catch (NotUniqueUsernameException e) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);
            attr.addFlashAttribute("error_username", e.getMessage());
            return REDIRECT + ALL_DOCTORS + "/" + doctor.getId() + "/edit";
        }
        return REDIRECT + ALL_DOCTORS;
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return REDIRECT + ALL_DOCTORS;
    }
}
