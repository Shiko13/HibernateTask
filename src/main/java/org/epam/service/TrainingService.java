package org.epam.service;

import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    TrainingDtoOutput save(TrainingDtoInput trainingDtoInput);

    List<TrainingDtoOutput> findByDateRangeAndTraineeUserName(LocalDate startDate, LocalDate endDate, String traineeUsername);

    List<TrainingDtoOutput> findByDateRangeAndTrainerUserName(LocalDate startDate, LocalDate endDate, String trainerUsername);

}
