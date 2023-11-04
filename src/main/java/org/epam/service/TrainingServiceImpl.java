package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.mapper.TrainingMapper;
import org.epam.model.Training;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.repo.TrainingRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepo trainingRepo;

    @Override
    @Transactional
    public TrainingDtoOutput save(TrainingDtoInput trainingDtoInput) {
        Training trainingToSave = TrainingMapper.INSTANCE.toEntity(trainingDtoInput);
        Training savedTraining = trainingRepo.save(trainingToSave);

        return TrainingDtoOutput.builder()
                .id(savedTraining.getId())
                .traineeId(savedTraining.getTraineeId())
                .trainerId(savedTraining.getTrainerId())
                .name(savedTraining.getName())
                .typeId(savedTraining.getTypeId())
                .date(savedTraining.getDate())
                .duration(savedTraining.getDuration())
                .build();
    }

    public List<TrainingDtoOutput> findByDateRangeAndTraineeUserName(LocalDate startDate, LocalDate endDate, String traineeUsername) {
        List<Training> trainings = trainingRepo.findByDateRangeAndTraineeUsername(startDate, endDate, traineeUsername);
        return TrainingMapper.INSTANCE.toDtoList(trainings);
    }

    public List<TrainingDtoOutput> findByDateRangeAndTrainerUserName(LocalDate startDate, LocalDate endDate, String trainerUsername) {
        List<Training> trainings = trainingRepo.findByDateRangeAndTrainerUsername(startDate, endDate, trainerUsername);
        return TrainingMapper.INSTANCE.toDtoList(trainings);
    }
}
