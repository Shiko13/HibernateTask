package service;

import org.epam.model.Training;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.repo.TrainingRepo;
import org.epam.service.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepo trainingRepo;

    @Test
    void save_shouldOk() {
        TrainingDtoInput trainingDtoInput = createTestTrainingDtoInput();
        Training savedTraining = createTestTraining(trainingDtoInput);

        when(trainingRepo.save(any(Training.class))).thenReturn(savedTraining);

        TrainingDtoOutput result = trainingService.save(trainingDtoInput);

        assertEquals(savedTraining.getId(), result.getId());
    }

    @Test
    void findByDateRangeAndTraineeUserName_shouldReturnTrainings() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String traineeUsername = "testTrainee";

        List<Training> testTrainings = createTestTrainings();

        when(trainingRepo.findByDateRangeAndTraineeUsername(startDate, endDate, traineeUsername))
                .thenReturn(testTrainings);

        List<TrainingDtoOutput>
                result = trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername);

        assertEquals(testTrainings.size(), result.size());
    }

    @Test
    void findByDateRangeAndTrainerUserName_shouldReturnTrainings() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String trainerUsername = "testTrainer";

        List<Training> testTrainings = createTestTrainings();

        when(trainingRepo.findByDateRangeAndTrainerUsername(startDate, endDate, trainerUsername))
                .thenReturn(testTrainings);

        List<TrainingDtoOutput> result = trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername);

        assertEquals(testTrainings.size(), result.size());
    }

    public TrainingDtoInput createTestTrainingDtoInput() {
        TrainingDtoInput trainingDtoInput = new TrainingDtoInput();
        trainingDtoInput.setTraineeId(1L);
        trainingDtoInput.setTrainerId(2L);
        trainingDtoInput.setName("Test Training");
        trainingDtoInput.setTypeId(3L);
        trainingDtoInput.setDate(LocalDate.of(2023, 1, 15));
        trainingDtoInput.setDuration(120L);

        return trainingDtoInput;
    }

    public Training createTestTraining(TrainingDtoInput trainingDtoInput) {
        Training training = new Training();
        training.setId(1L);
        training.setTraineeId(trainingDtoInput.getTraineeId());
        training.setTrainerId(trainingDtoInput.getTrainerId());
        training.setName(trainingDtoInput.getName());
        training.setTypeId(trainingDtoInput.getTypeId());
        training.setDate(trainingDtoInput.getDate());
        training.setDuration(trainingDtoInput.getDuration());

        return training;
    }

    public List<Training> createTestTrainings() {
        List<Training> trainings = new ArrayList<>();

        Training training1 = new Training();
        training1.setId(1L);
        trainings.add(training1);

        Training training2 = new Training();
        training2.setId(2L);
        trainings.add(training2);

        return trainings;
    }
}