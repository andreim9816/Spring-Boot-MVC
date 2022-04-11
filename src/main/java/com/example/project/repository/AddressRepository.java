package com.example.project.repository;

import com.example.project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT CASE WHEN (COUNT(adr) > 0) THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM Address adr WHERE adr.id = :addressId AND adr.patient.id IS NOT NULL")
    Boolean checkIfAddressIsTakenByPatient(@Param("addressId") Long addressId);
}
