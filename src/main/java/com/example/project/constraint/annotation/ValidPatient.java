package com.example.project.constraint.annotation;

import com.example.project.constraint.validator.PatientValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatientValidator.class)
@Documented
public @interface ValidPatient {

    String message() default "Invalid patient ID!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
