package com.example.project.controller;

import com.example.project.exception.CustomException;
import com.example.project.model.Address;
import com.example.project.model.Patient;
import com.example.project.service.DepartmentService;
import com.example.project.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.example.project.controller.DepartmentController.BINDING_RESULT_PATH;
import static com.example.project.controller.DepartmentController.REDIRECT;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
@Slf4j
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
        if (!model.containsAttribute("patient")) {
            model.addAttribute("patient", new Patient());
        }
        if (!model.containsAttribute("address")) {
            model.addAttribute("address", new Address());
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        return ADD_EDIT_PATIENT;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String patientId, Model model) {
        var patient = patientService.getPatientById(Long.valueOf(patientId));
        var departments = departmentService.getAllDepartments();

        if (!model.containsAttribute("patient")) {
            model.addAttribute("patient", patient);
        }
        if (!model.containsAttribute("address")) {
            model.addAttribute("address", patient.getAddress());
        }
        model.addAttribute("departmentAll", departments);

        return ADD_EDIT_PATIENT;
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute("patient") @Valid Patient patient, BindingResult bindingResultPatient,
                               @ModelAttribute("address") @Valid Address address, BindingResult bindingResultAddress,
                               RedirectAttributes attr) {
        if (bindingResultPatient.hasErrors() || bindingResultAddress.hasErrors()) {
            log.info("Model binding has errors!");

            attr.addFlashAttribute(BINDING_RESULT_PATH + "patient", bindingResultPatient);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "address", bindingResultAddress);
            attr.addFlashAttribute("patient", patient);
            attr.addFlashAttribute("address", address);

            if (patient.getId() != null) {
                log.info(String.format("Redirected back to endpoint %s", ALL_PATIENTS + "/" + patient.getId() + "/edit"));
                return REDIRECT + ALL_PATIENTS + "/" + patient.getId() + "/edit";
            } else {
                log.info(String.format("Redirected back to endpoint %s", ALL_PATIENTS + "/new"));
                return REDIRECT + ALL_PATIENTS + "/new";
            }
        }

        address.setPatient(patient);
        patient.setAddress(address);

        try {
            patientService.savePatient(patient);
//        addressService.saveAddress(address);
        } catch (CustomException e) {
            log.info("Error when saving into database! Error message = " + e.getMessage());

            attr.addFlashAttribute(BINDING_RESULT_PATH + "patient", bindingResultPatient);
            attr.addFlashAttribute("patient", patient);
            attr.addFlashAttribute("error_cnp", e.getMessage());

            if (patient.getId() == null) {
                log.info(String.format("Redirected back to endpoint %s", ALL_PATIENTS + "/new"));
                return REDIRECT + ALL_PATIENTS + "/new";
            } else {
                log.info(String.format("Redirected back to endpoint %s", ALL_PATIENTS + "/" + patient.getId() + "/edit"));
                return REDIRECT + ALL_PATIENTS + "/" + patient.getId() + "/edit";
            }
        }

        return REDIRECT + ALL_PATIENTS;
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return REDIRECT + ALL_PATIENTS;
    }
}
