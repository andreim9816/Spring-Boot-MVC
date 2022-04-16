package com.example.project.bootstrap;

import com.example.project.model.security.Authority;
import com.example.project.model.security.User;
import com.example.project.repository.DoctorRepository;
import com.example.project.repository.security.AuthorityRepository;
import com.example.project.repository.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.project.configuration.SecurityConfig.ROLE_ADMIN;
import static com.example.project.configuration.SecurityConfig.ROLE_DOCTOR;

@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private AuthorityRepository authorityRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private DoctorRepository doctorRepository;

    private void loadUserData() {
        if (userRepository.count() == 0) {
            Authority adminRole = authorityRepository.save(Authority.builder().role(ROLE_ADMIN).build());
            Authority doctorRole = authorityRepository.save(Authority.builder().role(ROLE_DOCTOR).build());

            User admin = User.builder()
                    .username("admin_1")
                    .password(passwordEncoder.encode("12345"))
                    .authority(adminRole)
                    .build();

            User doctor = User.builder()
                    .username("doctor_1")
                    .password(passwordEncoder.encode("12345"))
                    .doctor(doctorRepository.getById(1L))
                    .authority(doctorRole)
                    .build();

            userRepository.save(admin);
            userRepository.save(doctor);
        }
    }


    @Override
    public void run(String... args) {
        loadUserData();
    }
}
