//package com.example.project.constraint.validator;
//
//import com.example.project.constraint.annotation.ValidAddressNotTaken;
//import com.example.project.service.AddressService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//public class AddressNotTakenValidator implements ConstraintValidator<ValidAddressNotTaken, Long> {
//
//    private final AddressService addressService;
//
//    @Autowired
//    public AddressNotTakenValidator(AddressService addressService) {
//        this.addressService = addressService;
//    }
//
//    @Override
//    public boolean isValid(Long addressId, ConstraintValidatorContext constraintValidatorContext) {
//        if (addressId == null) {
//            return false;
//        }
//        return !addressService.checkIfAddressIsTakenByPatient(addressId);
//    }
//}
