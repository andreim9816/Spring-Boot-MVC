package com.example.project.controller;

import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import com.example.project.model.Medication;
import com.example.project.model.Patient;
import com.example.project.service.ConsultService;
import com.example.project.service.DoctorService;
import com.example.project.service.MedicationService;
import com.example.project.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView addConsult() {
        ModelAndView modelAndView = new ModelAndView(ADD_EDIT_CONSULT);

        var medications = medicationService.getAllMedications();
        var doctors = doctorService.getAllDoctors();
        var patients = patientService.getAllPatients();

        var consult = new Consult();
        consult.setDoctor(new Doctor());
        consult.setPatient(new Patient());

        modelAndView.addObject("consult", consult);
        modelAndView.addObject("doctorAll", doctors);
        modelAndView.addObject("patientAll", patients);
        modelAndView.addObject("medicationAll", medications);
        modelAndView.addObject("selectedPatientId", -1L);
        modelAndView.addObject("selectedDoctorId", -1L);
        modelAndView.addObject("selectedMedicationIds", new ArrayList<>());

        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editConsult(@PathVariable("id") String consultId) {
        ModelAndView modelAndView = new ModelAndView(ADD_EDIT_CONSULT);

        var consult = consultService.getConsultById(Long.valueOf(consultId));
        var medications = medicationService.getAllMedications();
        var doctors = doctorService.getAllDoctors();
        var patients = patientService.getAllPatients();
        var selectedMedicationIds = consult.getMedications().stream().map(Medication::getId).collect(Collectors.toList());

        modelAndView.addObject("consult", consult);
        modelAndView.addObject("doctorAll", doctors);
        modelAndView.addObject("patientAll", patients);
        modelAndView.addObject("medicationAll", medications);
        modelAndView.addObject("selectedPatientId", consult.getPatient().getId());
        modelAndView.addObject("selectedDoctorId", consult.getDoctor().getId());
        modelAndView.addObject("selectedMedicationIds", selectedMedicationIds);

        return modelAndView;
    }

    @PostMapping
    public String saveOrUpdateConsult(@ModelAttribute Consult consult) {
        consultService.saveConsult(consult);
        return REDIRECT + ALL_CONSULTS;
    }

    @DeleteMapping("/{id}")
    public String deleteConsult(@PathVariable Long id) {
        consultService.deleteConsultById(id);
        return REDIRECT + ALL_CONSULTS;
    }
}
