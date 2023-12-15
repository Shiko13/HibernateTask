package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDtoOutput {

    private Long id;

    private TrainingTypeOutputDto trainingType;

    private UserDtoOutput user;

    private List<Long> traineeIds;
}
