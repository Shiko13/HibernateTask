package controller;

import org.epam.controller.TraineeController;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.UserDtoInput;
import org.epam.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;


    @Test
    void getByUserName_ShouldReturnTraineeDtoOutput() {
        String userName = "testUser";
        String password = "testPassword";
        TraineeDtoOutput expectedOutput = createExpectedTraineeDtoOutput();

        when(traineeService.getByUserName(userName, password)).thenReturn(expectedOutput);

        TraineeDtoOutput result = traineeController.getByUserName(userName, password);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void save_ShouldReturnTraineeDtoOutput() {
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        TraineeDtoOutput expectedOutput = createExpectedTraineeDtoOutput();

        when(traineeService.save(traineeDtoInput)).thenReturn(expectedOutput);

        TraineeDtoOutput result = traineeController.save(traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }


    @Test
    void updateProfile_ShouldReturnTraineeDtoOutput() {
        String userName = "testUser";
        String password = "testPassword";
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        TraineeDtoOutput expectedOutput = createExpectedTraineeDtoOutput();

        when(traineeService.updateProfile(userName, password, traineeDtoInput)).thenReturn(expectedOutput);

        TraineeDtoOutput result = traineeController.updateProfile(userName, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void updateTrainerList_ShouldReturnTraineeDtoOutput() {
        String userName = "testUser";
        String password = "testPassword";
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        TraineeDtoOutput expectedOutput = createExpectedTraineeDtoOutput();

        when(traineeService.updateTrainerList(userName, password, traineeDtoInput)).thenReturn(expectedOutput);

        TraineeDtoOutput result = traineeController.updateTrainerList(userName, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void deleteByUsername_ShouldReturnNoContentResponse() {
        String userName = "testUser";
        String password = "testPassword";

        doNothing().when(traineeService).deleteByUsername(userName, password);

        ResponseEntity<Void> result = traineeController.deleteByUsername(userName, password);

        assertEquals(ResponseEntity.noContent().build(), result);
    }

    public TraineeDtoInput createTestTraineeDtoInput() {
        return TraineeDtoInput.builder()
                              .dateOfBirth(LocalDate.of(1990, 5, 15))
                              .address("123 Main Street")
                              .user(UserDtoInput.builder()
                                                .firstName("FirstName")
                                                .lastName("LastName")
                                                .isActive(true)
                                                .build())
                              .trainerIds(Arrays.asList(2L, 3L, 4L))
                              .build();
    }

    public TraineeDtoOutput createExpectedTraineeDtoOutput() {
        TraineeDtoOutput traineeDtoOutput = new TraineeDtoOutput();
        traineeDtoOutput.setDateOfBirth(LocalDate.of(1990, 5, 15));
        traineeDtoOutput.setAddress("123 Main Street");

        traineeDtoOutput.setTrainerIds(Arrays.asList(2L, 3L, 4L));

        return traineeDtoOutput;
    }
}