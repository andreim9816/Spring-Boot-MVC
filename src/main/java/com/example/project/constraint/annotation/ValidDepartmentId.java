package com.example.project.constraint.annotation;

import com.example.project.constraint.validator.DepartmentIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartmentIdValidator.class)
@Documented
public @interface ValidDepartmentId {

    String message() default "Invalid department ID!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
