package com.example.project.constraint.validator;

import com.example.project.constraint.annotation.UniqueDepartmentName;
import com.example.project.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueDepartmentNameValidator implements ConstraintValidator<UniqueDepartmentName, String> {

    private DepartmentService departmentService;

    public UniqueDepartmentNameValidator() {

    }

    public UniqueDepartmentNameValidator(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public boolean isValid(String departmentName, ConstraintValidatorContext constraintValidatorContext) {
        if (departmentName == null) {
            return false;
        }
        return departmentService.getDepartmentByName(departmentName).isEmpty();
    }
}
