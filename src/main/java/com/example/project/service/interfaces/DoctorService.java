package com.example.project.service.interfaces;

import com.example.project.model.Consult;
import com.example.project.model.Doctor;
import com.example.project.model.security.User;

import java.util.List;

public interface DoctorService {

    List<Doctor> getAllDoctors();

    List<Consult> getAllConsultsForDoctor(Long doctorId);

    Doctor getById(Long id);

    User saveOrUpdateUser(User user, Doctor doctor, String password);

    Doctor saveDoctor(Doctor doctor);

    void deleteDoctorById(Long id);
}
