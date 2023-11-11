package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.error.ErrorMessageConstants;
import org.epam.mapper.TrainingMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.model.dto.TrainingShortDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.TrainingRepo;
import org.epam.repo.TrainingTypeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepo trainingRepo;

    private final TraineeRepo traineeRepo;

    private final TrainerRepo trainerRepo;

    private final TrainingTypeRepo trainingTypeRepo;

    private final TrainingMapper trainingMapper;

    @Override
    @Transactional
    public TrainingShortDtoOutput save(TrainingDtoInput trainingDtoInput) {
        log.info("save, trainingDtoInput = {}", trainingDtoInput);

        Training trainingToSave = trainingMapper.toEntity(trainingDtoInput);
        Trainee trainee = traineeRepo.findById(trainingDtoInput.getTraineeId())
                                     .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
        Trainer trainer = trainerRepo.findById(trainingDtoInput.getTrainerId())
                                     .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
        TrainingType trainingType = trainingTypeRepo.findById(trainingDtoInput.getTypeId())
                                                    .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));

        trainingToSave.setTrainee(trainee);
        trainingToSave.setTrainer(trainer);
        trainingToSave.setTrainingType(trainingType);

        Training savedTraining = trainingRepo.save(trainingToSave);

        return trainingMapper.toShortDtoOutput(savedTraining);
    }

    @Override
    public List<TrainingDtoOutput> findByDateRangeAndTraineeUserName(LocalDate startDate, LocalDate endDate,
                                                                     String traineeUsername) {
        log.info("findByDateRangeAndTraineeUserName, startDate = {}, endDate = {}, traineeUsername = {}", startDate,
                endDate, traineeUsername);
        List<Training> trainings = trainingRepo.findByDateBetweenAndTraineeUserUserName(startDate, endDate, traineeUsername);

        return trainingMapper.toDtoList(trainings);
    }

    @Override
    public List<TrainingDtoOutput> findByDateRangeAndTrainerUserName(LocalDate startDate, LocalDate endDate,
                                                                     String trainerUsername) {
        log.info("findByDateRangeAndTrainerUserName, startDate = {}, endDate = {}, trainerUsername = {}", startDate,
                endDate, trainerUsername);
        List<Training> trainings = trainingRepo.findByDateBetweenAndTrainerUserUserName(startDate, endDate, trainerUsername);

        return trainingMapper.toDtoList(trainings);
    }
}
