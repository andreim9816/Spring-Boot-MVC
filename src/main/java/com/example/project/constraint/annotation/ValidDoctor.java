package com.example.project.constraint.annotation;

import com.example.project.constraint.validator.DoctorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoctorValidator.class)
@Documented
public @interface ValidDoctor {

    String message() default "Invalid doctor ID!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
