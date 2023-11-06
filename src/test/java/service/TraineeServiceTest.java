package service;

import org.epam.error.AccessException;
import org.epam.error.NotFoundException;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.UserRepo;
import org.epam.service.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeRepo traineeRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private TrainerRepo trainerRepo;

    @Test
    void save_shouldOk() {
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        User user = createTestUser();
        List<Trainer> selectedTrainers = createTestSelectedTrainers();
        Trainee traineeToSave = createTestTraineeToSave();

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(trainerRepo.findAllById(anyList())).thenReturn(selectedTrainers);
        when(traineeRepo.save(any(Trainee.class))).thenReturn(traineeToSave);

        TraineeDtoOutput result = traineeService.save(traineeDtoInput);

        assertNotNull(result);
        assertEquals(traineeToSave.getId(), result.getId());
    }

    @Test
    void getByUserName_shouldOk() {
        String userName = "testUser";
        String password = "testPassword";
        User user = createTestUser();
        Trainee trainee = createTestTrainee();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        TraineeDtoOutput result = traineeService.getByUserName(userName, password);

        assertNotNull(result);
        assertEquals(trainee.getId(), result.getId());
        assertEquals(trainee.getAddress(), result.getAddress());
        assertEquals(trainee.getUserId(),result.getUser().getId());
        assertEquals(trainee.getTrainers().stream().map(Trainer::getUserId).collect(Collectors.toList()), result.getTrainerIds());
    }

    @Test
    void getByUserName_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception =
                assertThrows(AccessException.class, () -> traineeService.getByUserName(userName, password));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void changePassword_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String oldPassword = "incorrectPassword";
        String newPassword = "newPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        assertThrows(AccessException.class, () -> traineeService.changePassword(userName, oldPassword, newPassword));
    }

    @Test
    void changePassword_WithValidInput_ShouldUpdateUserPassword() {
        String userName = "testUser";
        String oldPassword = "correctPassword";
        String newPassword = "newPassword";
        User user = createTestUser();
        user.setPassword(oldPassword);
        Trainee trainee = createTestTrainee();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        TraineeDtoOutput result = traineeService.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    void changePassword_WithValidInput_ShouldRetrieveTrainee() {
        String userName = "testUser";
        String oldPassword = "correctPassword";
        String newPassword = "newPassword";
        User user = createTestUser();
        user.setPassword(oldPassword);
        Trainee trainee = createTestTrainee();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        when(userRepo.save(any(User.class))).thenReturn(user);

        TraineeDtoOutput result = traineeService.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertNotNull(trainee);
    }

    @Test
    void updateProfile_WithValidInput_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "correctPassword";
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        assertThrows(AccessException.class, () -> traineeService.updateProfile(userName, password, traineeDtoInput));
    }

    @Test
    void updateProfile_WithValidInput_ShouldReturnUpdatedTraineeDtoOutput() {
        String userName = "testUser";
        String password = "correctPassword";
        User user = createTestUser();
        user.setPassword(password);
        Trainee trainee = createTestTrainee();
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        when(traineeRepo.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDtoOutput result = traineeService.updateProfile(userName, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(traineeDtoInput.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(traineeDtoInput.getAddress(), result.getAddress());
        assertEquals(traineeDtoInput.getUserId(), result.getUser().getId());
    }

    @Test
    void switchActivate_WithValidInput_ShouldReturnUpdatedTraineeDtoOutput() {
        String userName = "testUser";
        String password = "correctPassword";
        User user = createTestUser();
        user.setPassword(password);
        Trainee trainee = createTestTrainee();
        User updatedUser = createTestUser();
        updatedUser.setIsActive(false);

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));
        when(userRepo.save(user)).thenReturn(updatedUser);

        TraineeDtoOutput result = traineeService.switchActivate(userName, password);

        assertNotNull(result);
        assertFalse(result.getUser().getIsActive());
    }


    @Test
    void deleteByUsername_WithValidInput_ShouldDeleteTraineeAndUser() {
        String userName = "testUser";
        String password = "correctPassword";
        User user = createTestUser();
        user.setPassword(password);
        Trainee trainee = createTestTrainee();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> traineeService.deleteByUsername(userName, password));

        verify(traineeRepo, times(1)).deleteById(trainee.getId());
        verify(userRepo, times(1)).deleteById(user.getId());
    }

    @Test
    void updateTrainerList_WithValidInput_ShouldUpdateTrainerList() {
        String userName = "testUser";
        String password = "correctPassword";
        Trainee trainee = createTestTrainee();
        User user = createTestUser();
        user.setPassword(password);
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();
        List<Trainer> selectedTrainers = createTestSelectedTrainers();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainee));

        when(trainerRepo.findAllById(traineeDtoInput.getTrainerIds())).thenReturn(selectedTrainers);

        when(traineeRepo.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDtoOutput result = traineeService.updateTrainerList(userName, password, traineeDtoInput);

        assertNotNull(result);
        assertEquals(selectedTrainers, trainee.getTrainers());
    }

    @Test
    void updateTrainerList_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();
        TraineeDtoInput traineeDtoInput = createTestTraineeDtoInput();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception = assertThrows(AccessException.class,
                () -> traineeService.updateTrainerList(userName, password, traineeDtoInput));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void switchActivate_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception =
                assertThrows(AccessException.class, () -> traineeService.switchActivate(userName, password));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void deleteByUsername_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception =
                assertThrows(AccessException.class, () -> traineeService.deleteByUsername(userName, password));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    public void getByUserName_WhenTraineeNotFound_ShouldThrowNotFoundException() {
        // Arrange
        String userName = "nonexistentUser";
        String password = "password";

        assertThrows(NotFoundException.class, () -> traineeService.getByUserName(userName, password));
    }

    public TraineeDtoInput createTestTraineeDtoInput() {
        TraineeDtoInput traineeDtoInput = new TraineeDtoInput();
        traineeDtoInput.setUserId(1L);
        List<Long> trainerIds = List.of(2L, 3L, 4L);
        traineeDtoInput.setTrainerIds(trainerIds);

        return traineeDtoInput;
    }

    public User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPassword("testPassword");
        user.setIsActive(true);

        return user;
    }

    public List<Trainer> createTestSelectedTrainers() {
        List<Trainer> selectedTrainers = new ArrayList<>();

        Trainer trainer1 = new Trainer();
        trainer1.setId(2L);

        selectedTrainers.add(trainer1);
        Trainer trainer2 = new Trainer();
        trainer2.setId(3L);
        selectedTrainers.add(trainer2);

        return selectedTrainers;
    }

    public Trainee createTestTraineeToSave() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setAddress("Test Address");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setTrainers(new ArrayList<>());
        trainee.setUserId(2L);

        return trainee;
    }

    private Trainee createTestTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUserId(1L);
        trainee.setAddress("Test Address");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setTrainers(new ArrayList<>());

        return trainee;
    }
}
