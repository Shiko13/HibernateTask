package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TraineeTrainingDtoOutput {

    private Long id;

    private LocalDate dateOfBirth;

    private String address;

    private UserDtoOutput user;
}
