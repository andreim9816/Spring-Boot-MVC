package com.example.project.bootstrap;

import com.example.project.model.Doctor;
import com.example.project.model.security.Authority;
import com.example.project.model.security.User;
import com.example.project.repository.security.AuthorityRepository;
import com.example.project.repository.security.UserRepository;
import com.example.project.service.DoctorService;
import com.example.project.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.project.configuration.SecurityConfig.ROLE_ADMIN;
import static com.example.project.configuration.SecurityConfig.ROLE_DOCTOR;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;

    private void loadUserData() {
        if (userRepository.count() == 0) {
            Authority adminRole = authorityRepository.save(Authority.builder().role(ROLE_ADMIN).build());
            Authority doctorRole = authorityRepository.save(Authority.builder().role(ROLE_DOCTOR).build());

            User admin = User.builder()
                    .username("admin_1")
                    .password(passwordEncoder.encode("123456"))
                    .email("admin_1@email.com")
                    .authority(adminRole)
                    .build();

            Doctor doctorWithId1 = doctorService.getById(1L);
            Doctor doctorWithId2 = doctorService.getById(2L);
            Doctor doctorWithId3 = doctorService.getById(3L);

            User doctor1 = User.builder()
                    .username("doctor_1")
                    .password(passwordEncoder.encode("123456"))
                    .doctor(doctorWithId1)
                    .email("doctor_1@email.com")
                    .authority(doctorRole)
                    .build();

            User doctor2 = User.builder()
                    .username("doctor_2")
                    .password(passwordEncoder.encode("123456"))
                    .doctor(doctorWithId2)
                    .email("doctor_2@email.com")
                    .authority(doctorRole)
                    .build();

            User doctor3 = User.builder()
                    .username("doctor_3")
                    .password(passwordEncoder.encode("123456"))
                    .doctor(doctorWithId3)
                    .email("doctor_3@email.com")
                    .authority(doctorRole)
                    .build();

            doctorWithId1.setUser(doctor1);
            doctorWithId2.setUser(doctor2);
            doctorWithId3.setUser(doctor3);

            userRepository.save(admin);
//            userRepository.save(doctor1);
//            userRepository.save(doctor2);
//            userRepository.save(doctor3);

            doctorService.saveDoctor(doctorWithId1);
            doctorService.saveDoctor(doctorWithId2);
            doctorService.saveDoctor(doctorWithId3);
        }
    }


    @Override
    public void run(String... args) {
        loadUserData();
    }
}
