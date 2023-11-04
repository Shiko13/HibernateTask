package org.epam.service;

import org.epam.model.Trainee;
import org.epam.model.Training;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public interface TrainingService {

    TrainingDtoOutput save(TrainingDtoInput trainingDtoInput);

    List<TrainingDtoOutput> findByDateRangeAndTraineeUserName(LocalDate startDate, LocalDate endDate, String traineeUsername);

    List<TrainingDtoOutput> findByDateRangeAndTrainerUserName(LocalDate startDate, LocalDate endDate, String trainerUsername);

}
