package com.example.project.controller;

import com.example.project.model.Patient;
import com.example.project.service.DepartmentService;
import com.example.project.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.example.project.controller.DepartmentController.REDIRECT;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final static String ALL_PATIENTS = "patients";
    private final static String VIEW_PATIENT = "patient_info";
    private final static String ADD_EDIT_PATIENT = "patient_form";

    private final PatientService patientService;
    private final DepartmentService departmentService;

    @GetMapping
    public String getAll(Model model) {
        var patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return ALL_PATIENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") String patientId, Model model) {
        var patient = patientService.getPatientById(Long.valueOf(patientId));
        model.addAttribute("patient", patient);
        return VIEW_PATIENT;
    }

    @GetMapping("/new")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        return "patient_form";
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String patientId, Model model) {

        var patient = patientService.getPatientById(Long.valueOf(patientId));
        var departments = departmentService.getAllDepartments();

        model.addAttribute("patient", patient);
        model.addAttribute("departmentAll", departments);
        return "patient_form";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute Patient patient) {
        patientService.savePatient(patient);
        return REDIRECT + ALL_PATIENTS;
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return REDIRECT + ALL_PATIENTS;
    }
}
