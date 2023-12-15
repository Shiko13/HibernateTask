package controller;

import org.epam.controller.TrainingController;
import org.epam.model.dto.TraineeTrainingDtoOutput;
import org.epam.model.dto.TraineeTrainingShortDtoOutput;
import org.epam.model.dto.TrainerTrainingDtoOutput;
import org.epam.model.dto.TrainerTrainingShortDtoOutput;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.model.dto.TrainingShortDtoOutput;
import org.epam.model.dto.TrainingTypeOutputDto;
import org.epam.model.dto.UserDtoOutput;
import org.epam.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private TrainingService trainingService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void findByDateRangeAndTrainee_ShouldReturnListOfTrainingDtoOutput() throws Exception {
        List<TrainingDtoOutput> expectedList = createExpectedTrainingDtoOutputList();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String traineeUsername = "testUser";

        when(trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername)).thenReturn(
                expectedList);

        mockMvc.perform(MockMvcRequestBuilders.get("/training/criteria-trainee")
                                              .param("startDate", "2023-01-01")
                                              .param("endDate", "2023-12-31")
                                              .param("traineeUsername", "testUser")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedList.get(0).getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(expectedList.get(1).getId()));
    }

    @Test
    void findByDateRangeAndTrainer_ShouldReturnListOfTrainingDtoOutput() throws Exception {
        List<TrainingDtoOutput> expectedList = createExpectedTrainingDtoOutputList();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String trainerUsername = "testTrainer";

        when(trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername)).thenReturn(
                expectedList);

        mockMvc.perform(MockMvcRequestBuilders.get("/training/criteria-trainer")
                                              .param("startDate", "2023-01-01")
                                              .param("endDate", "2023-12-31")
                                              .param("trainerUsername", "testTrainer")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedList.get(0).getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(expectedList.get(1).getId()));
    }

    @Test
    void save_ShouldReturnTrainingDtoOutput() {
        TrainingDtoInput trainingDtoInput = createTestTrainingDtoInput();
        TrainingShortDtoOutput expected = createTrainingDtoOutput();

        when(trainingService.save(trainingDtoInput)).thenReturn(expected);

        TrainingShortDtoOutput result = trainingController.save(trainingDtoInput);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainee(), result.getTrainee());
        assertEquals(expected.getTrainer(), result.getTrainer());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getDate().toString(), result.getDate().toString());
        assertEquals(expected.getDuration(), result.getDuration());
    }

    private List<TrainingDtoOutput> createExpectedTrainingDtoOutputList() {
        List<TrainingDtoOutput> trainingDtoOutputs = new ArrayList<>();
        TraineeTrainingDtoOutput trainee1 =
                new TraineeTrainingDtoOutput(1L, LocalDate.of(2002, 5, 5), "Jameson street 29",
                        new UserDtoOutput(1L, "James", "Johnson", "james.johnson", true));
        TraineeTrainingDtoOutput trainee2 = new TraineeTrainingDtoOutput(2L, LocalDate.of(2000, 6, 6), "Spam street 30",
                new UserDtoOutput(2L, "Jasmin", "Johansson", "jasmin.johansson", true));

        TrainingTypeOutputDto trainingType1 = new TrainingTypeOutputDto(1L, "Box");
        TrainingTypeOutputDto trainingType2 = new TrainingTypeOutputDto(2L, "Zumba");

        TrainerTrainingDtoOutput trainer1 = new TrainerTrainingDtoOutput(3L, trainingType1,
                new UserDtoOutput(3L, "Alisa", "Smith", "alisa.smith", true));
        TrainerTrainingDtoOutput trainer2 = new TrainerTrainingDtoOutput(4L, trainingType2,
                new UserDtoOutput(4L, "Nikita", "Ivanov", "nikita.ivanov", true));

        TrainingDtoOutput training1 =
                createExpectedTrainingDtoOutput(1L, trainee1, trainer1, "Training1", trainingType1, LocalDate.of(2023, 1, 1), 60);
        TrainingDtoOutput training2 =
                createExpectedTrainingDtoOutput(2L, trainee2, trainer2, "Training2", trainingType2, LocalDate.of(2023, 2, 1), 90);

        trainingDtoOutputs.add(training1);
        trainingDtoOutputs.add(training2);

        return trainingDtoOutputs;
    }

    private TrainingDtoOutput createExpectedTrainingDtoOutput(Long id, TraineeTrainingDtoOutput trainee,
                                                              TrainerTrainingDtoOutput trainer, String name,
                                                              TrainingTypeOutputDto trainingType, LocalDate date,
                                                              long duration) {
        TrainingDtoOutput expected = new TrainingDtoOutput();
        expected.setId(id);
        expected.setTrainee(trainee);
        expected.setTrainer(trainer);
        expected.setName(name);
        expected.setType(trainingType);
        expected.setDate(date);
        expected.setDuration(duration);

        return expected;
    }

    private TrainingDtoInput createTestTrainingDtoInput() {
        TrainingDtoInput trainingDtoInput = new TrainingDtoInput();
        trainingDtoInput.setTraineeId(1L);
        trainingDtoInput.setTrainerId(2L);
        trainingDtoInput.setName("Training1");
        trainingDtoInput.setTypeId(1L);
        trainingDtoInput.setDate(LocalDate.of(2023, 1, 1));
        trainingDtoInput.setDuration(60L);

        return trainingDtoInput;
    }

    private TrainingShortDtoOutput createTrainingDtoOutput() {
        TrainingShortDtoOutput expected = new TrainingShortDtoOutput();
        TrainingTypeOutputDto trainingTypeOutputDto = new TrainingTypeOutputDto(3L, "Yoga");
        expected.setId(1L);
        expected.setTrainee(new TraineeTrainingShortDtoOutput(1L, LocalDate.of(1999, 11, 15), "Madison street 5"));
        expected.setTrainer(new TrainerTrainingShortDtoOutput(2L, trainingTypeOutputDto));
        expected.setName("Training 1");
        expected.setType(trainingTypeOutputDto);
        expected.setDate(LocalDate.of(2023, 1, 1));
        expected.setDuration(60L);

        return expected;
    }
}

