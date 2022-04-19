package com.example.project.controller;

import com.example.project.exception.CustomException;
import com.example.project.model.Doctor;
import com.example.project.model.security.User;
import com.example.project.service.DepartmentService;
import com.example.project.service.security.AuthorityService;
import com.example.project.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;

import static com.example.project.configuration.SecurityConfig.ROLE_DOCTOR;
import static com.example.project.controller.DepartmentController.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final static String REGISTER = "register_form";
    private final static String LOGIN = "login";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DepartmentService departmentService;
    private final AuthorityService authorityService;
    private final UserService userService;

    @GetMapping({"", "/", "/index"})
    public String departmentList(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return ALL_DEPARTMENTS;
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        return REGISTER;
    }

    @PostMapping("/process_register")
    public String processRegister(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                                  @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResultDoctor,
                                  RedirectAttributes attr) {

        if (bindingResultUser.hasErrors() || bindingResultDoctor.hasErrors()) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);
            return REDIRECT + REGISTER;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setDoctor(doctor);
        user.setAuthorities(Set.of(authorityService.getByRole(ROLE_DOCTOR)));
        doctor.setUser(user);

//        User newUser = User.builder()
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .phoneNumber(user.getPhoneNumber())
//                .firstName(user.getgetFirstName())
//                .lastName(user.getLastName())
//                .password(encodedPassword)
//                .authority(authorityService.getByRole("ROLE_CUSTOMER"))
//                .build();
        try {
            userService.saveUser(user);
        } catch (CustomException e) {

        }
//            attr.addFlashAttribute("exPhone", e.getMessage());
//            attr.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
//            attr.addFlashAttribute("user", user);
//            return "redirect:/register";
//        } catch (EmailNotUniqueException e) {
//            attr.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
//            attr.addFlashAttribute("user", user);
//            attr.addFlashAttribute("exEmail", e.getMessage());
//            return "redirect:/register";
//        } catch (UsernameNotUniqueException e) {
//            attr.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
//            attr.addFlashAttribute("user", user);
//            attr.addFlashAttribute("exUsername", e.getMessage());
//            if (user.getId() != null)
//                return "redirect:/users/update/" + user.getId().toString();
//            return "redirect:/register";
//        }

        return REDIRECT + LOGIN;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "login-error";
    }

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }
}
