package com.example.project.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.example.project.model.Regex.NAME_REGEX;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    @NotBlank(message = "Department name must be provided!")
    @Pattern(regexp = NAME_REGEX, message = "Department name is invalid!")
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Patient> patients;
}
