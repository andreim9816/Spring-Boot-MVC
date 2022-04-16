package com.example.project.controller;

import com.example.project.model.Doctor;
import com.example.project.service.DepartmentService;
import com.example.project.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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

    @GetMapping("/new")
    public String addDoctor(Model model) {
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        return ADD_EDIT_DOCTOR;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String doctorId, Model model) {

        var doctor = doctorService.getById(Long.valueOf(doctorId));
        var departments = departmentService.getAllDepartments();

        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", doctor);
        }
        model.addAttribute("departmentAll", departments);

        return ADD_EDIT_DOCTOR;
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult, RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResult);
            attr.addFlashAttribute("doctor", doctor);

            if (doctor.getId() != null) {
                return REDIRECT + ALL_DOCTORS + "/" + doctor.getId() + "/edit";
            } else {
                return REDIRECT + ALL_DOCTORS + "/new";
            }
        }
        doctorService.saveDoctor(doctor);

        return REDIRECT + ALL_DOCTORS;
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return REDIRECT + ALL_DOCTORS;
    }
}
