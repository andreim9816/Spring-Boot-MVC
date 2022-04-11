package com.example.project.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.example.project.model.Regex.CNP_REGEX;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
public class Patient extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PATIENT_ID")
    private Long id;

    @Pattern(regexp = CNP_REGEX, message = "Invalid CNP!")
    @NotBlank(message = "CNP must be provided!")
    private String cnp;

    @ManyToOne
    @JoinColumn(name = "FK_DEPARTMENT_ID")
    @ToString.Exclude
    private Department department;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Consult> consults;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ADDRESS_ID")
    @ToString.Exclude
    private Address address;
}
