//package com.example.project.service;
//
//import com.example.project.dto.input.ReqAddressDto;
//import com.example.project.exception.EntityNotFoundException;
//import com.example.project.mapper.AddressMapper;
//import com.example.project.model.Address;
//import com.example.project.repository.AddressRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AddressService {
//
//    private final AddressRepository addressRepository;
//    private final AddressMapper addressMapper;
//
//    @Autowired
//    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
//        this.addressRepository = addressRepository;
//        this.addressMapper = addressMapper;
//    }
//
//    public List<Address> getAllAddresses() {
//        return addressRepository.findAll();
//    }
//
//    public Address getAddressById(Long id) {
//        return addressRepository.findById(id)
//                .orElseThrow(() -> EntityNotFoundException.builder()
//                        .entityId(id)
//                        .entityType("Address")
//                        .build()
//                );
//    }
//
//    public Boolean checkIfAddressExists(Long id) {
//        return addressRepository.findById(id).isPresent();
//    }
//
//    public Boolean checkIfAddressIsTakenByPatient(Long addressId) {
//        return addressRepository.checkIfAddressIsTakenByPatient(addressId);
//    }
//
//    public Address saveAddress(Address address) {
//        return addressRepository.save(address);
//    }
//
//    public Address updateAddress(ReqAddressDto reqConsultDto, Address address) {
//        Address updatedAddress = addressMapper.update(reqConsultDto, address);
//
//        return saveAddress(updatedAddress);
//    }
//
//    public void deleteAddressById(Long id) {
//        addressRepository.deleteById(id);
//    }
//
//}
