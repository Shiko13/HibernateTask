package org.epam.service;

import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.model.dto.TrainingShortDtoOutput;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    TrainingShortDtoOutput save(TrainingDtoInput trainingDtoInput);

    List<TrainingDtoOutput> findByDateRangeAndTraineeUserName(LocalDate startDate, LocalDate endDate, String traineeUsername);

    List<TrainingDtoOutput> findByDateRangeAndTrainerUserName(LocalDate startDate, LocalDate endDate, String trainerUsername);

}
