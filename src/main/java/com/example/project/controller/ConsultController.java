package com.example.project.controller;

import com.example.project.exception.CustomException;
import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import com.example.project.model.Medication;
import com.example.project.model.Patient;
import com.example.project.model.dto.SelectedMedication;
import com.example.project.service.ConsultService;
import com.example.project.service.DoctorService;
import com.example.project.service.MedicationService;
import com.example.project.service.PatientService;
import com.example.project.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.controller.DepartmentController.BINDING_RESULT_PATH;
import static com.example.project.controller.DepartmentController.REDIRECT;

@Controller
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final static String ALL_CONSULTS = "consults";
    private final static String VIEW_CONSULT = "consult_info";
    private final static String ADD_EDIT_CONSULT = "consult_form";

    private final ConsultService consultService;
    private final DoctorService doctorService;
    private final MedicationService medicationService;
    private final PatientService patientService;
    private final UserService userService;

    @GetMapping
    public String getAll(Model model) {
        var consults = consultService.getAllConsults();
        model.addAttribute("consults", consults);
        return ALL_CONSULTS;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable("id") String consultId) {
        ModelAndView modelAndView = new ModelAndView(VIEW_CONSULT);

        var consult = consultService.getConsultById(Long.valueOf(consultId));
        var selectedMedicationIds = consult.getMedications().stream().map(Medication::getId).collect(Collectors.toList());
        var medications = consult.getMedications().stream()
                .sorted(Comparator.comparing(Medication::getName).thenComparing(Medication::getQuantity))
                .collect(Collectors.toList());
        var doctor = consult.getDoctor();
        var patient = consult.getPatient();

        var doctorName = doctor.getLastName() + " " + doctor.getFirstName();
        var patientName = patient.getLastName() + " " + patient.getFirstName();

        modelAndView.addObject("consult", consult);
        modelAndView.addObject("doctorName", doctorName);
        modelAndView.addObject("patientName", patientName);
        modelAndView.addObject("medicationAll", medications);
        modelAndView.addObject("selectedMedicationIds", selectedMedicationIds);

        return modelAndView;
    }

    @GetMapping("/new")
    public String addConsult(Model model) {
        List<SelectedMedication> selectedMedications;
        var doctors = doctorService.getAllDoctors();
        var patients = patientService.getAllPatients();
        var consult = new Consult();

        if (!model.containsAttribute("consult")) {
            consult.setDoctor(new Doctor());
            consult.setPatient(new Patient());
            consult.setMedications(new ArrayList<>());
            selectedMedications = medicationService.getAllMedications().stream()
                    .map(med -> new SelectedMedication(med, false))
                    .collect(Collectors.toList());
            model.addAttribute("consult", consult);
        } else {
            consult = (Consult) model.getAttribute("consult");
            var containedMedicationIds = consult.getMedications() == null ? new ArrayList<Long>() : consult.getMedications().stream().map(Medication::getId).collect(Collectors.toList());
            selectedMedications = medicationService.getAllMedications().stream().map(med -> {
                var isContained = containedMedicationIds.contains(med.getId());
                return new SelectedMedication(med, isContained);
            }).collect(Collectors.toList());
            consult.setMedications(medicationService.findMedicationsByIdContains(containedMedicationIds));
        }

        model.addAttribute("selectedMedications", selectedMedications);
        model.addAttribute("doctorAll", doctors);
        model.addAttribute("patientAll", patients);

        /* Doctors can add consults only on their behalf */
        if (UserService.isLoggedIn() && userService.isDoctor()) {
            consult.setDoctor(userService.getCurrentUser().getDoctor());
        }

        return ADD_EDIT_CONSULT;
    }

    @GetMapping("/{id}/edit")
    public String editConsult(@PathVariable("id") String consultId, Model model) {

        var doctors = doctorService.getAllDoctors();
        var patients = patientService.getAllPatients();
        List<SelectedMedication> selectedMedications;

        /* First time display, no validation failed before */
        if (!model.containsAttribute("consult")) {
            var consult = consultService.getConsultById(Long.valueOf(consultId));
            var containedMedicationIds = consult.getMedications() == null ? new ArrayList<Long>() : consult.getMedications().stream().map(Medication::getId).collect(Collectors.toList());
            selectedMedications = medicationService.getAllMedications().stream().map(med -> {
                var isContained = containedMedicationIds.contains(med.getId());
                return new SelectedMedication(med, isContained);
            }).collect(Collectors.toList());
            model.addAttribute("consult", consult);
        } else {
            var consult = (Consult) model.getAttribute("consult");
            var containedMedicationIds = consult.getMedications() == null ? new ArrayList<Long>() : consult.getMedications().stream().map(Medication::getId).collect(Collectors.toList());
            selectedMedications = medicationService.getAllMedications().stream().map(med -> {
                var isContained = containedMedicationIds.contains(med.getId());
                return new SelectedMedication(med, isContained);
            }).collect(Collectors.toList());
            consult.setMedications(medicationService.findMedicationsByIdContains(containedMedicationIds));
        }

        model.addAttribute("selectedMedications", selectedMedications);
        model.addAttribute("doctorAll", doctors);
        model.addAttribute("patientAll", patients);

        return ADD_EDIT_CONSULT;
    }

    @PostMapping
    public String saveOrUpdateConsult(@ModelAttribute("consult") @Valid Consult consult, BindingResult bindingResult, RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "consult", bindingResult);
            attr.addFlashAttribute("consult", consult);

            if (consult.getId() != null) {
                return REDIRECT + ALL_CONSULTS + "/" + consult.getId() + "/edit";
            } else {
                return REDIRECT + ALL_CONSULTS + "/new";
            }
        }

        try {
            consultService.saveConsult(consult);
        } catch (CustomException e) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "consult", bindingResult);
            attr.addFlashAttribute("consult", consult);
            attr.addFlashAttribute("error_date", e.getMessage());

            if (consult.getId() != null) {
                return REDIRECT + ALL_CONSULTS + "/" + consult.getId() + "/edit";
            } else {
                return REDIRECT + ALL_CONSULTS + "/new";
            }
        }
        return REDIRECT + ALL_CONSULTS;
    }

    @DeleteMapping("/{id}")
    public String deleteConsult(@PathVariable Long id) {
        consultService.deleteConsultById(id);
        return REDIRECT + ALL_CONSULTS;
    }
}
