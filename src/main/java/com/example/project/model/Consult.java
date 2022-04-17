package com.example.project.model;

import com.example.project.service.Helper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSULT_ID")
    private Long id;

    @NotNull(message = "Date must be provided!")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private Date date = new Date();

    @Length(min = 1, message = "Please enter at least 10 characters!")
    private String diagnose;

    @Length(min = 10, message = "Please enter at least 10 characters!")
    private String symptoms;

    @Length(min = 5, message = "Please enter at least 5 characters!")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "FK_DOCTOR_ID")
    @ToString.Exclude
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "FK_PATIENT_ID")
    @ToString.Exclude
    private Patient patient;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "Prescription",
            joinColumns = @JoinColumn(name = "CONSULT_ID", referencedColumnName = "CONSULT_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEDICATION_ID", referencedColumnName = "MEDICATION_ID"))
    private List<Medication> medications;
}
