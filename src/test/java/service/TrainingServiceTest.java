package service;

import org.epam.mapper.TrainingMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.epam.model.dto.TraineeTrainingShortDtoOutput;
import org.epam.model.dto.TrainerTrainingShortDtoOutput;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.model.dto.TrainingShortDtoOutput;
import org.epam.model.dto.TrainingTypeOutputDto;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.TrainingRepo;
import org.epam.repo.TrainingTypeRepo;
import org.epam.service.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepo trainingRepo;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TraineeRepo traineeRepo;

    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private TrainingTypeRepo trainingTypeRepo;

    @Test
    void save_shouldReturnSavedTrainingDtoOutput() {
        TrainingDtoInput trainingDtoInput = createTrainingDtoInput();
        Training savedTraining = createTraining(trainingDtoInput);
        TrainingShortDtoOutput trainingShortDtoOutput = createShortTraining(savedTraining);

        when(trainingRepo.save(any(Training.class))).thenReturn(savedTraining);
        when(trainingMapper.toEntity(any())).thenReturn(savedTraining);
        when(traineeRepo.findById(trainingDtoInput.getTraineeId())).thenReturn(
                Optional.ofNullable(savedTraining.getTrainee()));
        when(trainerRepo.findById(trainingDtoInput.getTrainerId())).thenReturn(
                Optional.ofNullable(savedTraining.getTrainer()));
        when(trainingTypeRepo.findById(trainingDtoInput.getTypeId())).thenReturn(
                Optional.ofNullable(savedTraining.getTrainingType()));
        when(trainingMapper.toShortDtoOutput(savedTraining)).thenReturn(trainingShortDtoOutput);

        TrainingShortDtoOutput result = trainingService.save(trainingDtoInput);

        assertEquals(savedTraining.getId(), result.getId());
    }

    @Test
    void findByDateRangeAndTraineeUserName_shouldReturnTrainings() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String traineeUsername = "testTrainee";

        List<Training> testTrainings = createTestTrainings();

        when(trainingRepo.findByDateBetweenAndTraineeUserUserName(startDate, endDate, traineeUsername)).thenReturn(
                testTrainings);
        when(trainingMapper.toDtoList(testTrainings)).thenReturn(toDtoList(testTrainings));

        List<TrainingDtoOutput> result =
                trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername);

        assertEquals(testTrainings.size(), result.size());
    }

    @Test
    void findByDateRangeAndTrainerUserName_shouldReturnTrainings() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String trainerUsername = "testTrainer";

        List<Training> testTrainings = createTestTrainings();

        when(trainingRepo.findByDateBetweenAndTrainerUserUserName(startDate, endDate, trainerUsername)).thenReturn(
                testTrainings);
        when(trainingMapper.toDtoList(testTrainings)).thenReturn(toDtoList(testTrainings));

        List<TrainingDtoOutput> result =
                trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername);

        assertEquals(testTrainings.size(), result.size());
    }

    public TrainingDtoInput createTrainingDtoInput() {
        return TrainingDtoInput.builder()
                               .traineeId(1L)
                               .trainerId(2L)
                               .name("Test Training")
                               .typeId(3L)
                               .date(LocalDate.of(2023, 1, 15))
                               .duration(120L)
                               .build();

    }

    public Training createTraining(TrainingDtoInput trainingDtoInput) {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        TrainingType trainingType = new TrainingType(1L, "Yoga");

        return Training.builder()
                       .id(1L)
                       .trainee(
                               new Trainee(trainingDtoInput.getTraineeId(), LocalDate.of(2001, 4, 5), "Baker street 50",
                                       user1, new ArrayList<>()))
                       .trainer(new Trainer(trainingDtoInput.getTrainerId(), trainingType, user2, new ArrayList<>()))
                       .name(trainingDtoInput.getName())
                       .trainingType(trainingType)
                       .date(trainingDtoInput.getDate())
                       .duration(trainingDtoInput.getDuration())
                       .build();
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

    private List<TrainingDtoOutput> toDtoList(List<Training> trainings) {
        if (trainings == null) {
            return null;
        }

        List<TrainingDtoOutput> list = new ArrayList<>(trainings.size());
        for (Training training : trainings) {
            list.add(toDtoOutput(training));
        }

        return list;
    }

    private TrainingDtoOutput toDtoOutput(Training training) {
        return TrainingDtoOutput.builder()
                                .id(training.getId())
                                .name(training.getName())
                                .duration(training.getDuration())
                                .build();
    }

    TrainingShortDtoOutput createShortTraining(Training training) {
        TrainingTypeOutputDto trainingTypeOutputDto =
                new TrainingTypeOutputDto(training.getTrainingType().getId(), training.getTrainingType().getName());

        TraineeTrainingShortDtoOutput trainee = TraineeTrainingShortDtoOutput.builder()
                                                                             .id(training.getTrainee().getId())
                                                                             .dateOfBirth(training.getTrainee()
                                                                                                  .getDateOfBirth())
                                                                             .address(
                                                                                     training.getTrainee().getAddress())

                                                                             .build();
        TrainerTrainingShortDtoOutput trainer = TrainerTrainingShortDtoOutput.builder()
                                                                             .id(training.getTrainer().getId())
                                                                             .trainingType(trainingTypeOutputDto)
                                                                             .build();

        return TrainingShortDtoOutput.builder()
                                     .id(training.getId())
                                     .trainee(trainee)
                                     .trainer(trainer)
                                     .name(training.getName())
                                     .type(trainingTypeOutputDto)
                                     .date(training.getDate())
                                     .duration(training.getDuration())
                                     .build();
    }
}