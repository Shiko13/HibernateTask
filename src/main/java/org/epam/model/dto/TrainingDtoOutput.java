package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDtoOutput {

    private Long id;

    private TraineeTrainingDtoOutput trainee;

    private TrainerTrainingDtoOutput trainer;

    private String name;

    private TrainingTypeOutputDto type;

    private LocalDate date;

    private Long duration;
}
