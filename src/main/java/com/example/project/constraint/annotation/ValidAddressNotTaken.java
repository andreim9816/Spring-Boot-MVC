//package com.example.project.constraint.annotation;
//
//import com.example.project.constraint.validator.AddressNotTakenValidator;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//import java.lang.annotation.*;
//
//@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = AddressNotTakenValidator.class)
//@Documented
//public @interface ValidAddressNotTaken {
//
//    String message() default "Address already taken!";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}
