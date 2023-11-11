package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrainerTrainingShortDtoOutput {

    private Long id;

    private TrainingTypeOutputDto trainingType;
}
