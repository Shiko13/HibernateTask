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
public class TrainingShortDtoOutput {

    private Long id;

    private TraineeTrainingShortDtoOutput trainee;

    private TrainerTrainingShortDtoOutput trainer;

    private String name;

    private TrainingTypeOutputDto type;

    private LocalDate date;

    private Long duration;
}
