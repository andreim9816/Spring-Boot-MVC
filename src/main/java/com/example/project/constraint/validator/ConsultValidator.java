package com.example.project.constraint.validator;

import com.example.project.constraint.annotation.ValidConsult;
import com.example.project.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsultValidator implements ConstraintValidator<ValidConsult, Long> {

    private final ConsultService consultService;

    @Autowired
    public ConsultValidator(ConsultService consultService) {
        this.consultService = consultService;
    }

    @Override
    public boolean isValid(Long consultId, ConstraintValidatorContext constraintValidatorContext) {
        if (consultId == null) {
            return false;
        }
        return consultService.checkIfConsultExists(consultId);
    }
}
