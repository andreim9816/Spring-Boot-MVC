package com.example.project.controller;

import com.example.project.model.Department;
import com.example.project.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    public final static String REDIRECT = "redirect:/";
    public final static String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";

    public final static String ALL_DEPARTMENTS = "departments";
    private final static String VIEW_DEPARTMENT = "department_info";
    private final static String ADD_EDIT_DEPARTMENT = "department_form";


    private final DepartmentService departmentService;

    @GetMapping(value = {"", "/", "/index"})
    public String getAll(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return ALL_DEPARTMENTS;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable("id") String departmentId) {
        ModelAndView modelAndView = new ModelAndView(VIEW_DEPARTMENT);

        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));
        modelAndView.addObject("department", department);

        return modelAndView;
    }

    @GetMapping("/new")
    public String addDepartment(Model model) {
        if (!model.containsAttribute("department")) {
            model.addAttribute("department", new Department());
        }
        return ADD_EDIT_DEPARTMENT;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String departmentId, Model model) {
        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));

        if (!model.containsAttribute("department")) {
            model.addAttribute("department", department);
        }

        return ADD_EDIT_DEPARTMENT;
    }

    @PostMapping
    public String saveOrUpdateDepartment(@ModelAttribute("department") @Valid Department department, BindingResult bindingResult, RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "department", bindingResult);
            attr.addFlashAttribute("department", department);

            if (department.getId() != null) {
                return REDIRECT + ALL_DEPARTMENTS + "/" + department.getId() + "/edit";
            } else {
                return REDIRECT + ALL_DEPARTMENTS + "/new";
            }
        }
        departmentService.saveDepartment(department);

        return REDIRECT + ALL_DEPARTMENTS;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteDepartment(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT + ALL_DEPARTMENTS);
        departmentService.deleteDepartmentById(id);
        return modelAndView;
    }
}
