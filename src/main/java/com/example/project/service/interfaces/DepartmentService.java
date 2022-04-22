package com.example.project.service.interfaces;

import com.example.project.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Boolean checkIfDepartmentExists(Long id);

    Optional<Department> getDepartmentByName(String departmentName);

    Department saveDepartment(Department department);

    void deleteDepartmentById(Long id);
}
