package com.example.project.model.dto;

import com.example.project.model.Medication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SelectedMedication {

    private Medication medication;
    private boolean isPresent;
}
