package com.example.project.repository;

import com.example.project.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    Optional<Medication> findMedicationByNameAndQuantity(String name, Integer quantity);

//    @Query("select med FROM Medication med where med.id in (:ids)")
    List<Medication> findByIdIsIn(List<Long> ids);
}
