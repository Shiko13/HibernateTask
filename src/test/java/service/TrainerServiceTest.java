package service;

import org.epam.error.AccessException;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.UserRepo;
import org.epam.service.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private TraineeRepo traineeRepo;


    @Test
    void save_shouldOk() {
        TrainerDtoInput trainerDtoInput = createTestTrainerDtoInput();
        User user = createTestUser();
        List<Trainee> selectedTrainees = createTestSelectedTrainees();
        Trainer trainerToSave = createTestTrainerToSave();

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(traineeRepo.findAllById(anyList())).thenReturn(selectedTrainees);
        when(trainerRepo.save(any(Trainer.class))).thenReturn(trainerToSave);

        TrainerDtoOutput result = trainerService.save(trainerDtoInput);

        assertNotNull(result);
        assertEquals(trainerToSave.getId(), result.getId());
    }

    @Test
    void getByUserName_shouldOk() {
        String userName = "testUser";
        String password = "testPassword";
        User user = createTestUser();
        Trainer trainer = createTestTrainer();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainer));

        TrainerDtoOutput result = trainerService.getByUserName(userName, password);

        assertNotNull(result);
        assertEquals(trainer.getId(), result.getId());
        assertEquals(trainer.getTrainingTypeId(), result.getTrainingTypeId());
    }

    @Test
    void getByUserName_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception =
                assertThrows(AccessException.class, () -> trainerService.getByUserName(userName, password));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void changePassword_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String oldPassword = "incorrectPassword";
        String newPassword = "newPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        assertThrows(AccessException.class, () -> trainerService.changePassword(userName, oldPassword, newPassword));
    }

    @Test
    void changePassword_WithValidInput_ShouldUpdateUserPassword() {
        String userName = "testUser";
        String oldPassword = "correctPassword";
        String newPassword = "newPassword";
        User user = createTestUser();
        user.setPassword(oldPassword);
        Trainer trainer = createTestTrainer();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainer));

        TrainerDtoOutput result = trainerService.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    void changePassword_WithValidInput_ShouldRetrieveTrainer() {
        String userName = "testUser";
        String oldPassword = "correctPassword";
        String newPassword = "newPassword";
        User user = createTestUser();
        user.setPassword(oldPassword);
        Trainer trainer = createTestTrainer();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainer));

        when(userRepo.save(any(User.class))).thenReturn(user);

        TrainerDtoOutput result = trainerService.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertNotNull(trainer);
    }

    @Test
    void updateProfile_WithValidInput_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "correctPassword";
        TrainerDtoInput trainerDtoInput = createTestTrainerDtoInput();
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        assertThrows(AccessException.class, () -> trainerService.updateProfile(userName, password, trainerDtoInput));
    }

    @Test
    void updateProfile_WithValidInput_ShouldReturnUpdatedTrainerDtoOutput() {
        String userName = "testUser";
        String password = "correctPassword";
        User user = createTestUser();
        user.setPassword(password);
        Trainer trainer = createTestTrainer();
        TrainerDtoInput trainerDtoInput = createTestTrainerDtoInput();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainer));

        when(trainerRepo.save(any(Trainer.class))).thenReturn(trainer);

        TrainerDtoOutput result = trainerService.updateProfile(userName, password, trainerDtoInput);

        assertNotNull(result);
        assertEquals(trainerDtoInput.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(trainerDtoInput.getUserId(), result.getUser().getId());
    }

    @Test
    void switchActivate_WithValidInput_ShouldReturnUpdatedTrainerDtoOutput() {
        String userName = "testUser";
        String password = "correctPassword";
        User user = createTestUser();
        user.setPassword(password);
        Trainer trainer = createTestTrainer();
        User updatedUser = createTestUser();
        updatedUser.setIsActive(false);

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainer));
        when(userRepo.save(user)).thenReturn(updatedUser);

        TrainerDtoOutput result = trainerService.switchActivate(userName, password);

        assertNotNull(result);
        assertFalse(result.getUser().getIsActive());
    }

    @Test
    void switchActivate_WithIncorrectPassword_ShouldThrowAccessException() {
        String userName = "testUser";
        String password = "incorrectPassword";
        User user = createTestUser();

        when(userRepo.findByUserName(userName)).thenReturn(Optional.of(user));

        AccessException exception = assertThrows(AccessException.class,
                () -> trainerService.switchActivate(userName, password));

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void getTrainersWithEmptyTrainees_ShouldReturnEmptyList() {
        when(trainerRepo.findTrainersWithEmptyTraineesList()).thenReturn(new ArrayList<>());

        List<TrainerDtoOutput> result = trainerService.getTrainersWithEmptyTrainees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTrainersWithEmptyTrainees_ShouldReturnTrainersWithUserDetails() {
        List<Trainer> trainers = createTestTrainers();
        List<Long> userIds = List.of(1L, 2L);

        when(trainerRepo.findTrainersWithEmptyTraineesList()).thenReturn(trainers);
        when(userRepo.findAllById(anyList())).thenAnswer((Answer<List<User>>) invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return createTestUsers(ids);
        });

        List<TrainerDtoOutput> result = trainerService.getTrainersWithEmptyTrainees();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(trainerRepo, times(1)).findTrainersWithEmptyTraineesList();
        verify(userRepo, times(1)).findAllById(userIds);
    }

    private TrainerDtoInput createTestTrainerDtoInput() {
        TrainerDtoInput trainerDtoInput = new TrainerDtoInput();
        trainerDtoInput.setUserId(1L);
        List<Long> traineeIds = List.of(2L, 3L, 4L);
        trainerDtoInput.setTraineeIds(traineeIds);

        return trainerDtoInput;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPassword("testPassword");
        user.setIsActive(true);

        return user;
    }

    private List<Trainee> createTestSelectedTrainees() {
        Trainer trainer1 = createTestTrainer();
        Trainer trainer2 = createTestTrainer();

        Trainee trainee1 = new Trainee();
        trainee1.setId(1L);
        trainee1.setTrainers(new ArrayList<>(Arrays.asList(trainer1, trainer2)));

        Trainee trainee2 = new Trainee();
        trainee2.setId(2L);
        trainee2.setTrainers(new ArrayList<>(Arrays.asList(trainer1, trainer2)));

        return Arrays.asList(trainee1, trainee2);
    }

    private Trainer createTestTrainerToSave() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setTrainingTypeId(123L);
        List<Trainee> trainees = createTestSelectedTrainees();
        trainer.setTrainees(trainees);

        return trainer;
    }

    public Trainer createTestTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setTrainingTypeId(5L);
        trainer.setUserId(1L);

        List<Trainee> trainees = new ArrayList<>();
        Trainee trainee1 = new Trainee();
        trainee1.setId(1L);
        trainee1.setUserId(11L);
        trainee1.setAddress("Trainee Address 1");
        trainee1.setDateOfBirth(LocalDate.of(1998, 8, 12));
        trainee1.setTrainers(Collections.singletonList(trainer));

        Trainee trainee2 = new Trainee();
        trainee2.setId(2L);
        trainee2.setUserId(12L);
        trainee2.setAddress("Trainee Address 2");
        trainee2.setDateOfBirth(LocalDate.of(1999, 6, 24));
        trainee2.setTrainers(Collections.singletonList(trainer));

        trainees.add(trainee1);
        trainees.add(trainee2);

        trainer.setTrainees(trainees);

        return trainer;
    }

    private List<Trainer> createTestTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setTrainingTypeId(1L);
        trainer1.setUserId(1L);
        trainer1.setTrainees(new ArrayList<>());

        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setTrainingTypeId(2L);
        trainer2.setUserId(2L);
        trainer2.setTrainees(new ArrayList<>());

        trainers.add(trainer1);
        trainers.add(trainer2);

        return trainers;
    }

    private List<User> createTestUsers(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = new User();
            user.setId(userId);
            user.setUserName("TestUser" + userId);
            user.setPassword("TestPassword" + userId);
            user.setIsActive(true);
            users.add(user);
        }
        return users;
    }
}
