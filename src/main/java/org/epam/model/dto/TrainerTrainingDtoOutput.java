package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainerTrainingDtoOutput {

    private Long id;

    private TrainingTypeOutputDto trainingType;

    private UserDtoOutput user;
}
