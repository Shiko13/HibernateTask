package controller;

import org.epam.controller.TrainingController;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

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
    public void findByDateRangeAndTrainee_ShouldReturnListOfTrainingDtoOutput() throws Exception {
        List<TrainingDtoOutput> expectedList = createExpectedTrainingDtoOutputList();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String traineeUsername = "testUser";

        when(trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername))
                .thenReturn(expectedList);

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
    public void findByDateRangeAndTrainer_ShouldReturnListOfTrainingDtoOutput() throws Exception {
        List<TrainingDtoOutput> expectedList = createExpectedTrainingDtoOutputList();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        String trainerUsername = "testTrainer";

        when(trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername))
                .thenReturn(expectedList);

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
        TrainingDtoOutput expected = createTrainingDtoOutput();

        when(trainingService.save(any(TrainingDtoInput.class))).thenReturn(expected);

        TrainingDtoOutput result = trainingController.save(trainingDtoInput);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTraineeId(), result.getTraineeId());
        assertEquals(expected.getTrainerId(), result.getTrainerId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getTypeId(), result.getTypeId());

        assertEquals(expected.getDate().toString(), result.getDate().toString());
        assertEquals(expected.getDuration(), result.getDuration());
    }

    private List<TrainingDtoOutput> createExpectedTrainingDtoOutputList() {
        List<TrainingDtoOutput> trainingDtoOutputs = new ArrayList<>();

        TrainingDtoOutput training1 = createExpectedTrainingDtoOutput(1L, 1L, 2L, "Training1", 1L, LocalDate.of(2023, 1, 1), 60);
        TrainingDtoOutput training2 = createExpectedTrainingDtoOutput(2L, 2L, 3L, "Training2", 2L, LocalDate.of(2023, 2, 1), 90);

        trainingDtoOutputs.add(training1);
        trainingDtoOutputs.add(training2);

        return trainingDtoOutputs;
    }

    private TrainingDtoOutput createExpectedTrainingDtoOutput(Long id, Long traineeId, Long trainerId, String name,
                                                              Long typeId, LocalDate date, long duration) {
        TrainingDtoOutput expected = new TrainingDtoOutput();
        expected.setId(id);
        expected.setTraineeId(traineeId);
        expected.setTrainerId(trainerId);
        expected.setName(name);
        expected.setTypeId(typeId);
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

    private TrainingDtoOutput createTrainingDtoOutput() {
        TrainingDtoOutput expected = new TrainingDtoOutput();
        expected.setId(1L);
        expected.setTraineeId(1L);
        expected.setTrainerId(2L);
        expected.setName("Training 1");
        expected.setTypeId(3L);
        expected.setDate(LocalDate.of(2023, 1, 1));
        expected.setDuration(60L);

        return expected;
    }
}

