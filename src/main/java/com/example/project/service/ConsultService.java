package com.example.project.service;

import com.example.project.exception.CustomException;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.Consult;
import com.example.project.repository.ConsultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultService {

    private final ConsultRepository consultRepository;

    public Page<Consult> getAllConsults(Pageable pageable) {
        return consultRepository.findAll(pageable);
    }

    public Consult getConsultById(Long id) {
        return consultRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.builder()
                        .entityId(id)
                        .entityType("Consult")
                        .build()
                );
    }

    public Consult saveConsult(Consult consult) {
        if (consult.getDate().after(new Date())) {
            throw new CustomException("Date cannot be in the future!");
        }

        return consultRepository.save(consult);
    }

    public void deleteConsultById(Long id) {
        consultRepository.deleteById(id);
    }
}
