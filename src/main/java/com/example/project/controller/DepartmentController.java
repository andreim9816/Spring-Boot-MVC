package com.example.project.controller;

import com.example.project.model.Department;
import com.example.project.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final static String ALL_DEPARTMENTS = "departments";
    private final static String VIEW_DEPARTMENT = "department_info";
    private final static String ADD_EDIT_DEPARTMENT = "department_form";
    public final static String REDIRECT = "redirect:/";

    private final DepartmentService departmentService;

    @GetMapping
    public String getAll(Model model) {
        var departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return ALL_DEPARTMENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") String departmentId, Model model) {
        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));
        model.addAttribute("department", department);
        return VIEW_DEPARTMENT;
    }

    @GetMapping("/new")
    public String addDepartment(Model model) {
        model.addAttribute("department", Department.builder().build());
        return ADD_EDIT_DEPARTMENT;
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") String departmentId, Model model) {
        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));
        model.addAttribute("department", department);
        return ADD_EDIT_DEPARTMENT;
    }

    @PostMapping
    public String saveOrUpdateDepartment(@ModelAttribute Department department) {
        departmentService.saveDepartment(department);
        return REDIRECT + ALL_DEPARTMENTS;
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartmentById(id);
        return REDIRECT + ALL_DEPARTMENTS;
    }
}
