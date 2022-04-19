package com.example.project.service.security;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.exception.NotUniqueEmailException;
import com.example.project.exception.NotUniqueUsernameException;
import com.example.project.model.Doctor;
import com.example.project.model.security.Authority;
import com.example.project.model.security.User;
import com.example.project.repository.security.UserRepository;
import com.example.project.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.project.configuration.SecurityConfig.ROLE_DOCTOR;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DoctorService doctorService;

    public User findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new UsernameNotFoundException("Username: " + username);
        }
    }

    public boolean isDoctor() {
        return getCurrentUser().getAuthorities().stream()
                .map(Authority::getRole).collect(Collectors.toList()).contains(ROLE_DOCTOR);
    }

    public boolean isAdmin() {
        return !isDoctor();
    }

    public User saveOrUpdateUser(User user) {

        var userUsername = getUserByUsername(user.getUsername());
        var userEmail = getUserByEmail(user.getEmail());

        if (userUsername.isPresent() && !Objects.equals(userUsername.get().getId(), user.getId())) {
            throw new NotUniqueUsernameException(String.format("Username %s already exists!", user.getUsername()));
        }

        if (userEmail.isPresent() && !Objects.equals(userEmail.get().getEmail(), user.getEmail())) {
            throw new NotUniqueEmailException(String.format("Email %s already exists!", user.getEmail()));
        }

//        Doctor doctorSaved = doctorService.saveDoctor(user.getDoctor());
//        user.setDoctor(doctorSaved);
        return doctorService.saveDoctor(user.getDoctor()).getUser();
//        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw EntityNotFoundException.builder().entityId(null).entityType("User").build();
            }
            return user.get();
        } else throw EntityNotFoundException.builder().entityId(null).entityType("User").build();
    }

    public static boolean isLoggedIn() {
        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null) {
                return false;
            }
            return principal instanceof UserDetails;
        } catch (Exception e) {
            return false;
        }
    }

    private Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
