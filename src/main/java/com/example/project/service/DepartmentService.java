package com.example.project.service;

import com.example.project.exception.CustomException;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Department;
import com.example.project.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {

        return departmentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.builder()
                        .entityId(id)
                        .entityType("Department")
                        .build()
                );
    }

    public Boolean checkIfDepartmentExists(Long id) {
        return departmentRepository.findById(id).isPresent();
    }

    public Optional<Department> getDepartmentByName(String departmentName) {
        return departmentRepository.findDepartmentByName(departmentName);
    }

    public Department saveDepartment(Department department) {

        Optional<Department> departmentByName = getDepartmentByName(department.getName());
        if (departmentByName.isPresent()) {
            throw new CustomException(String.format("Department with name %s already exists!", department.getName()));
        }

        return departmentRepository.save(department);
    }

    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
}
