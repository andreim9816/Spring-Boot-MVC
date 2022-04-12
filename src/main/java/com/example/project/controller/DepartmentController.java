package com.example.project.controller;

import com.example.project.model.Department;
import com.example.project.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView(ALL_DEPARTMENTS);
        modelAndView.addObject("departments", departmentService.getAllDepartments());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable("id") String departmentId) {
        ModelAndView modelAndView = new ModelAndView(VIEW_DEPARTMENT);
        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));
        modelAndView.addObject("department", department);
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView addDepartment() {
        ModelAndView modelAndView = new ModelAndView(ADD_EDIT_DEPARTMENT);
        modelAndView.addObject("department", new Department());
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editDepartment(@PathVariable("id") String departmentId) {
        ModelAndView modelAndView = new ModelAndView(ADD_EDIT_DEPARTMENT);
        var department = departmentService.getDepartmentById(Long.valueOf(departmentId));
        modelAndView.addObject("department", department);
        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveOrUpdateDepartment(@ModelAttribute Department department) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT + ALL_DEPARTMENTS);
        departmentService.saveDepartment(department);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteDepartment(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT + ALL_DEPARTMENTS);
        departmentService.deleteDepartmentById(id);
        return modelAndView;
    }
}
