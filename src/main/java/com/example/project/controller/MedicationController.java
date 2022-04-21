package com.example.project.controller;

import com.example.project.exception.CustomException;
import com.example.project.model.Medication;
import com.example.project.service.MedicationService;
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
@RequestMapping("/medications")
@RequiredArgsConstructor
@Slf4j
public class MedicationController {

    private final static String ALL_MEDICATIONS = "medications";
    private final static String VIEW_MEDICATION = "medication_info";
    private final static String ADD_EDIT_MEDICATION = "medication_form";

    private final MedicationService medicationService;

    @GetMapping
    public String getAll(Model model) {
        var medications = medicationService.getAllMedications();
        model.addAttribute("medications", medications);
        return ALL_MEDICATIONS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") String medicationId, Model model) {
        var medication = medicationService.getMedicationById(Long.valueOf(medicationId));
        model.addAttribute("medication", medication);
        return VIEW_MEDICATION;
    }

    @GetMapping("/new")
    public String addMedication(Model model) {
        if (!model.containsAttribute("medication")) {
            model.addAttribute("medication", new Medication());
        }

        return ADD_EDIT_MEDICATION;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String medicationId, Model model) {
        var medication = medicationService.getMedicationById(Long.valueOf(medicationId));

        if (!model.containsAttribute("medication")) {
            model.addAttribute("medication", medication);
        }

        return ADD_EDIT_MEDICATION;
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute("medication") @Valid Medication medication, BindingResult bindingResult,
                               RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            log.info("Model binding has errors!");

            attr.addFlashAttribute(BINDING_RESULT_PATH + "medication", bindingResult);
            attr.addFlashAttribute("medication", medication);

            if (medication.getId() != null) {
                log.info(String.format("Redirected back to endpoint %s", ALL_MEDICATIONS + "/" + medication.getId() + "/edit"));
                return REDIRECT + ALL_MEDICATIONS + "/" + medication.getId() + "/edit";
            } else {
                log.info(String.format("Redirected back to endpoint %s", ALL_MEDICATIONS + "/new"));
                return REDIRECT + ALL_MEDICATIONS + "/new";
            }
        }

        try {
            medicationService.saveMedication(medication);
        } catch (CustomException e) {
            log.info("Error when saving into database! Error message = " + e.getMessage());

            attr.addFlashAttribute(BINDING_RESULT_PATH + "medication", bindingResult);
            attr.addFlashAttribute("medication", medication);
            attr.addFlashAttribute("error_medication", e.getMessage());

            if (medication.getId() == null) {
                log.info(String.format("Redirected back to endpoint %s", ALL_MEDICATIONS + "/new"));
                return REDIRECT + ALL_MEDICATIONS + "/new";
            } else {
                log.info(String.format("Redirected back to endpoint %s", ALL_MEDICATIONS + "/" + medication.getId() + "/edit"));
                return REDIRECT + ALL_MEDICATIONS + "/" + medication.getId() + "/edit";
            }
        }

        return REDIRECT + ALL_MEDICATIONS;
    }

    @DeleteMapping("/{id}")
    public String deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedicationById(id);
        return REDIRECT + ALL_MEDICATIONS;
    }
}
