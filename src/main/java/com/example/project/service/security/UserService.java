package com.example.project.service.security;

import com.example.project.exception.EntityNotFoundException;
import com.example.project.model.security.Authority;
import com.example.project.model.security.User;
import com.example.project.repository.security.UserRepository;
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

    public boolean checkIfCurrentUserIsSameDoctor(Long doctorId) {
        return Objects.equals(doctorId, getCurrentUser().getDoctor().getId());
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

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
