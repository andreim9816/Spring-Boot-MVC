package com.example.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.project.model.Regex.NAME_REGEX;


@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class Person {

    @Length(min = 3, message = "First name should have minimum 3 letters!")
    @Length(max = 30, message = "First name should have maximum 30 letters!")
    @Pattern(regexp = NAME_REGEX, message = "Invalid first name")
    @NotBlank(message = "First name must be provided!")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Length(min = 2, message = "Last name should have minimum 2 letters!")
    @Length(max = 30, message = "Last name should have maximum 30 letters!")
    @Pattern(regexp = NAME_REGEX, message = "Invalid last name")
    @NotBlank(message = "Last name must be provided!")
    @Column(name = "LAST_NAME")
    private String lastName;
}
