package com.example.project.model;

import com.example.project.model.security.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
public class Doctor extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCTOR_ID")
    private Long id;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Consult> consults;

    @ManyToOne
    @JoinColumn(name = "FK_DEPARTMENT_ID")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_USER_ID")
    private User user;
}
