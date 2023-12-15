package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
public class TraineeTrainingShortDtoOutput {

    private Long id;

    private LocalDate dateOfBirth;

    private String address;
}
