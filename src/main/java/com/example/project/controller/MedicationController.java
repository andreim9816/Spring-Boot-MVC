package com.example.project.controller;

import com.example.project.model.Medication;
import com.example.project.service.DepartmentService;
import com.example.project.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.example.project.controller.DepartmentController.REDIRECT;

@Controller
@RequestMapping("/medications")
@RequiredArgsConstructor
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
        model.addAttribute("medication", new Medication());

        return ADD_EDIT_MEDICATION;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String medicationId, Model model) {
        var medication = medicationService.getMedicationById(Long.valueOf(medicationId));
        model.addAttribute("medication", medication);
        return ADD_EDIT_MEDICATION;
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute Medication medication) {
        medicationService.saveMedication(medication);
        return REDIRECT + ALL_MEDICATIONS;
    }

    @DeleteMapping("/{id}")
    public String deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedicationById(id);
        return REDIRECT + ALL_MEDICATIONS;
    }
}
