package controller;

import org.epam.controller.TrainerController;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @Test
    void getByUserName_ShouldReturnTrainerDtoOutput() {
        TrainerDtoOutput expected = createExpectedTrainerDtoOutput();
        when(trainerService.getByUserName(anyString(), anyString())).thenReturn(expected);

        TrainerDtoOutput result = trainerController.getByUserName("testUser", "testPassword");

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(expected.getUser().getId(), result.getUser().getId());
    }

    @Test
    void getTrainersWithEmptyTrainees_ShouldReturnListOfTrainerDtoOutput() {
        List<TrainerDtoOutput> expectedList = createExpectedTrainerDtoOutputList();
        when(trainerService.getTrainersWithEmptyTrainees()).thenReturn(expectedList);

        List<TrainerDtoOutput> resultList = trainerController.getTrainersWithEmptyTrainees();

        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertThat(resultList, is(expectedList));
    }

    @Test
    void save_ShouldReturnTrainerDtoOutput() {
        TrainerDtoInput trainerDtoInput = createTestTrainerDtoInput();
        TrainerDtoOutput expected = createExpectedTrainerDtoOutput();
        when(trainerService.save(any(TrainerDtoInput.class))).thenReturn(expected);

        TrainerDtoOutput result = trainerController.save(trainerDtoInput);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(expected.getUser().getId(), result.getUser().getId());
    }

    @Test
    void changePassword_ShouldReturnTrainerDtoOutput() {
        TrainerDtoOutput expected = createExpectedTrainerDtoOutput();
        when(trainerService.changePassword(anyString(), anyString(), anyString())).thenReturn(expected);

        TrainerDtoOutput result = trainerController.changePassword("testUser", "oldPassword", "newPassword");

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(expected.getUser().getId(), result.getUser().getId());
    }

    @Test
    void updateProfile_ShouldReturnTrainerDtoOutput() {
        TrainerDtoInput trainerDtoInput = createTestTrainerDtoInput();
        TrainerDtoOutput expected = createExpectedTrainerDtoOutput();
        when(trainerService.updateProfile(anyString(), anyString(), any(TrainerDtoInput.class))).thenReturn(expected);

        TrainerDtoOutput result = trainerController.updateProfile("testUser", "testPassword", trainerDtoInput);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(expected.getUser().getId(), result.getUser().getId());
    }

    @Test
    void switchActivate_ShouldReturnTrainerDtoOutput() {
        TrainerDtoOutput expected = createExpectedTrainerDtoOutput();
        when(trainerService.switchActivate(anyString(), anyString())).thenReturn(expected);

        TrainerDtoOutput result = trainerController.switchActivate("testUser", "testPassword");

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(expected.getUser().getId(), result.getUser().getId());
    }

    private TrainerDtoOutput createExpectedTrainerDtoOutput() {
        TrainerDtoOutput expected = new TrainerDtoOutput();
        expected.setId(1L);
        expected.setTrainingTypeId(2L);
        expected.setUser(createExpectedUserDtoOutput());

        return expected;
    }

    private List<TrainerDtoOutput> createExpectedTrainerDtoOutputList() {
        List<TrainerDtoOutput> trainerDtoOutputs = new ArrayList<>();

        TrainerDtoOutput trainer1 = new TrainerDtoOutput();
        trainer1.setId(1L);
        trainer1.setTrainingTypeId(2L);
        trainer1.setUser(createExpectedUserDtoOutput());
        trainer1.setTraineeIds(Arrays.asList(3L, 4L));

        TrainerDtoOutput trainer2 = new TrainerDtoOutput();
        trainer2.setId(2L);
        trainer2.setTrainingTypeId(3L);
        trainer2.setUser(createExpectedUserDtoOutput());
        trainer2.setTraineeIds(Arrays.asList(4L, 5L));

        trainerDtoOutputs.add(trainer1);
        trainerDtoOutputs.add(trainer2);

        return trainerDtoOutputs;
    }


    private TrainerDtoInput createTestTrainerDtoInput() {
        TrainerDtoInput trainerDtoInput = new TrainerDtoInput();
        trainerDtoInput.setUserId(1L);
        trainerDtoInput.setTrainingTypeId(2L);
        trainerDtoInput.setTraineeIds(Arrays.asList(3L, 4L));

        return trainerDtoInput;
    }


    private UserDtoOutput createExpectedUserDtoOutput() {
        UserDtoOutput userDtoOutput = new UserDtoOutput();
        userDtoOutput.setId(1L);
        userDtoOutput.setFirstName("John");
        userDtoOutput.setLastName("Doe");
        userDtoOutput.setUserName("john.doe");
        userDtoOutput.setIsActive(true);

        return userDtoOutput;
    }
}
