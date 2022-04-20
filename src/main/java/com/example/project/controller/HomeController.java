package com.example.project.controller;

import com.example.project.exception.NotUniqueEmailException;
import com.example.project.exception.NotUniqueUsernameException;
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

    public final static String ACCESS_DENIED = "access-denied";
    private final static String REGISTER_FORM = "register_form";
    private final static String REGISTER = "register";
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
        } else {
            User user = (User) model.getAttribute("user");
            user.setPassword("");
        }
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        model.addAttribute("departmentAll", departmentService.getAllDepartments());

        return REGISTER_FORM;
    }

    @PostMapping("/register")
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
        user.setAuthorities(Set.of(authorityService.getByRole(ROLE_DOCTOR)));

        user.setDoctor(doctor);
        doctor.setUser(user);

        try {
            userService.saveOrUpdateUser(user);
        } catch (NotUniqueEmailException e) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);
            attr.addFlashAttribute("error_email", e.getMessage());
            return REDIRECT + REGISTER;
        } catch (NotUniqueUsernameException e) {
            attr.addFlashAttribute(BINDING_RESULT_PATH + "user", bindingResultUser);
            attr.addFlashAttribute(BINDING_RESULT_PATH + "doctor", bindingResultDoctor);
            attr.addFlashAttribute("user", user);
            attr.addFlashAttribute("doctor", doctor);
            attr.addFlashAttribute("error_username", e.getMessage());
            return REDIRECT + REGISTER;
        }
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

    @GetMapping("/" + ACCESS_DENIED)
    public String accessDenied() {
        return "access_denied";
    }
}
