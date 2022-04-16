package com.example.project.constraint.annotation;

import com.example.project.constraint.validator.UniqueDepartmentNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDepartmentNameValidator.class)
@Documented
public @interface UniqueDepartmentName {

    String message() default "class Department name already exists!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
