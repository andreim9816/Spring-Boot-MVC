//package com.example.project.constraint.annotation;
//
//import com.example.project.constraint.validator.AddressValidator;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//import java.lang.annotation.*;
//
//@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = AddressValidator.class)
//@Documented
//public @interface ValidAddress {
//
//    String message() default "Invalid address ID!";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}
