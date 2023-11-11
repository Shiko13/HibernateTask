package service;

import org.epam.error.AccessException;
import org.epam.mapper.TrainerMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.model.dto.TrainingTypeOutputDto;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.TrainingTypeRepo;
import org.epam.repo.UserRepo;
import org.epam.service.AuthenticationService;
import org.epam.service.TrainerServiceImpl;
import org.epam.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
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

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingTypeRepo trainingTypeRepo;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    private User user;
    private List<Trainee> selectedTrainees;
    private TrainerDtoInput trainerDtoInput;
    private Trainer trainerToSave;
    private TrainerDtoOutput savedTrainer;

    private UserDtoOutput userDtoOutput;

    @BeforeEach
    void setUp() {
        user = createUser();
        userDtoOutput = createUserDtoOutput(user);
        selectedTrainees = createSelectedTrainees();
        trainerDtoInput = createTrainerDtoInput(user, selectedTrainees);
        trainerToSave = createTrainerToSave(trainerDtoInput, selectedTrainees, user);
        savedTrainer = createSavedTrainer(trainerToSave, userDtoOutput);
    }


    @Test
    void save_shouldReturnSavedTrainerDtoOutput() {
        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(trainerRepo.save(trainerToSave)).thenReturn(trainerToSave);
        when(trainerMapper.toEntity(trainerDtoInput)).thenReturn(trainerToSave);
        when(trainerMapper.toDtoOutput(trainerToSave)).thenReturn(savedTrainer);
        when(trainingTypeRepo.findById(trainerDtoInput.getTrainingTypeId())).thenReturn(
                Optional.ofNullable(trainerToSave.getTrainingType()));
        when(traineeRepo.findAllByIdIn(trainerDtoInput.getTraineeIds())).thenReturn(selectedTrainees);

        TrainerDtoOutput result = trainerService.save(trainerDtoInput);

        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getTrainingType(), result.getTrainingType());
        assertEquals(savedTrainer.getUser(), result.getUser());
        assertEquals(savedTrainer.getTraineeIds(), result.getTraineeIds());
    }

    @Test
    void getByUserName_shouldOk() {
        when(trainerMapper.toDtoOutput(trainerToSave)).thenReturn(savedTrainer);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.ofNullable(trainerToSave));

        TrainerDtoOutput result = trainerService.getByUserName(user.getUserName(), user.getPassword());

        assertNotNull(result);
        assertEquals(savedTrainer.getId(), result.getId());
        assertEquals(savedTrainer.getTrainingType(), result.getTrainingType());
        assertEquals(savedTrainer.getUser(), result.getUser());
        assertEquals(savedTrainer.getTraineeIds(), result.getTraineeIds());
    }

    @Test
    void updateProfile_WithValidInput_ShouldOk() {
        TrainerDtoInput updatedTrainer = createUpdatedTrainerDtoInput(user, selectedTrainees);
        Trainer updatedSavedTrainer = createTrainerToSave(updatedTrainer, selectedTrainees, user);
        TrainerDtoOutput savedUpdatedTrainer = createSavedTrainer(updatedSavedTrainer, userDtoOutput);

        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainerToSave));
        when(trainerRepo.save(any(Trainer.class))).thenReturn(updatedSavedTrainer);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(trainerMapper.toDtoOutput(updatedSavedTrainer)).thenReturn(savedUpdatedTrainer);
        when(trainingTypeRepo.findById(updatedTrainer.getTrainingTypeId())).thenReturn(
                Optional.ofNullable(updatedSavedTrainer.getTrainingType()));

        TrainerDtoOutput result = trainerService.updateProfile(user.getUserName(), user.getPassword(), updatedTrainer);

        assertNotNull(result);
        assertEquals(updatedTrainer.getTrainingTypeId(), result.getTrainingType().getId());
        assertEquals(updatedTrainer.getUserId(), result.getUser().getId());
    }

    @Test
    void updateProfile_WithValidInput_IdDifferentIds() {
        TrainerDtoInput updatedTrainer = createUpdatedTrainerDtoInput(user, selectedTrainees);
        updatedTrainer.setTrainingTypeId(trainerDtoInput.getTrainingTypeId());
        Trainer updatedSavedTrainer = createTrainerToSave(updatedTrainer, selectedTrainees, user);
        TrainerDtoOutput savedUpdatedTrainer = createSavedTrainer(updatedSavedTrainer, userDtoOutput);

        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainerToSave));
        when(trainerRepo.save(any(Trainer.class))).thenReturn(updatedSavedTrainer);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(trainerMapper.toDtoOutput(updatedSavedTrainer)).thenReturn(savedUpdatedTrainer);

        TrainerDtoOutput result = trainerService.updateProfile(user.getUserName(), user.getPassword(), updatedTrainer);

        assertNotNull(result);
        assertEquals(updatedTrainer.getTrainingTypeId(), result.getTrainingType().getId());
        assertEquals(updatedTrainer.getUserId(), result.getUser().getId());
    }

    @Test
    void updateProfile_WithValidInput_ShouldThrowAccessException() {
        String userName = user.getUserName();
        String password = user.getPassword();
        TrainerDtoInput updatedTrainer = createUpdatedTrainerDtoInput(user, selectedTrainees);

        when(trainerRepo.findByUserId(user.getId())).thenReturn(Optional.of(trainerToSave));
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));

        AccessException exception = assertThrows(
                AccessException.class,
                () -> trainerService.updateProfile(userName, password, updatedTrainer),
                "An AccessException should be thrown when the user does not exist"
        );

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void getTrainersWithEmptyTrainees_ShouldReturnEmptyList() {
        when(trainerRepo.findByTraineesIsEmpty()).thenReturn(new ArrayList<>());

        List<TrainerDtoOutput> result = trainerService.getTrainersWithEmptyTrainees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTrainersWithEmptyTrainees_ShouldReturnTrainersWithUserDetails() {
        List<Trainer> trainers = createTestTrainers();

        when(trainerRepo.findByTraineesIsEmpty()).thenReturn(trainers);

        List<TrainerDtoOutput> result = trainerService.getTrainersWithEmptyTrainees();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(trainerRepo).findByTraineesIsEmpty();
    }

    @Test
    void authenticate_ValidPasswordAndMatchingIds_NoExceptionThrown() {
        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        assertDoesNotThrow(() -> trainerService.authenticate(user.getPassword(), user, trainerDtoInput),
                "Exception should not be thrown for valid credentials and matching IDs");

        verify(authenticationService).checkAccess(user.getPassword(), user);
    }

    @Test
    void authenticate_InvalidPassword_AccessExceptionThrown() {
        when(authenticationService.checkAccess("Invalid password", user)).thenReturn(true);

        AccessException exception = assertThrows(AccessException.class,
                () -> trainerService.authenticate("Invalid password", user, trainerDtoInput),
                "An AccessException should be thrown for invalid password");

        verify(authenticationService).checkAccess("Invalid password", user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void authenticate_MismatchedIds_AccessExceptionThrown() {
        String password = user.getPassword();
        trainerDtoInput.setId(user.getId() + 1);

        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        AccessException exception = assertThrows(AccessException.class,
                () -> trainerService.authenticate(password, user, trainerDtoInput),
                "An AccessException should be thrown for mismatched IDs");

        verify(authenticationService).checkAccess(user.getPassword(), user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void checkUserExisting_UserExists_NoExceptionThrown() {
        when(userRepo.existsById(user.getId())).thenReturn(true);

        assertDoesNotThrow(() -> trainerService.checkUserExisting(user.getId()),
                "No exception should be thrown when the user exists");

        verify(userRepo).existsById(user.getId());
    }

    @Test
    void checkUserExisting_UserDoesNotExist_AccessExceptionThrown() {
        Long notExistingUserId = user.getId() + 1;

        when(userRepo.existsById(notExistingUserId)).thenReturn(false);

        AccessException exception = assertThrows(AccessException.class,
                () -> trainerService.checkUserExisting(notExistingUserId),
                "An AccessException should be thrown when the user does not exist");

        verify(userRepo).existsById(notExistingUserId);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void authenticate_ValidPassword_NoExceptionThrown() {
        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        assertDoesNotThrow(() -> trainerService.authenticate(user.getPassword(), user),
                "No exception should be thrown for a valid password");

        verify(authenticationService).checkAccess(user.getPassword(), user);
    }

    @Test
    void authenticate_InvalidPassword_AccessExceptionThrown2() {
        when(authenticationService.checkAccess("Invalid password", user)).thenReturn(true);

        AccessException exception = assertThrows(AccessException.class,
                () -> trainerService.authenticate("Invalid password", user),
                "An AccessException should be thrown for an invalid password");

        verify(authenticationService).checkAccess("Invalid password", user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    public TrainerDtoInput createTrainerDtoInput(User user, List<Trainee> trainees) {
        return TrainerDtoInput.builder()
                              .id(user.getId())
                              .trainingTypeId(1L)
                              .userId(user.getId())
                              .traineeIds(trainees.stream().map(Trainee::getId).collect(Collectors.toList()))
                              .build();
    }

    public TrainerDtoInput createUpdatedTrainerDtoInput(User user, List<Trainee> trainees) {
        return TrainerDtoInput.builder()
                              .id(user.getId())
                              .trainingTypeId(2L)
                              .userId(user.getId())
                              .traineeIds(trainees.stream().map(Trainee::getId).collect(Collectors.toList()))
                              .build();
    }

    public User createUser() {
        return User.builder().id(1L).userName("testUser").password("testPassword").isActive(true).prefix(0).build();
    }

    public UserDtoOutput createUserDtoOutput(User user) {
        return UserDtoOutput.builder()
                            .id(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .userName(user.getUserName())
                            .isActive(user.getIsActive())
                            .build();
    }

    public List<Trainee> createSelectedTrainees() {
        Trainee trainee1 = new Trainee();
        trainee1.setId(2L);
        trainee1.setTrainers(new ArrayList<>(List.of(Trainer.builder().id(3L).trainees(new ArrayList<>()).build())));

        Trainee trainee2 = new Trainee();
        trainee2.setId(3L);
        trainee2.setTrainers(new ArrayList<>(List.of(Trainer.builder().id(4L).trainees(new ArrayList<>()).build())));

        return List.of(trainee1, trainee2);
    }

    public Trainer createTrainerToSave(TrainerDtoInput trainerDtoInput, List<Trainee> trainees, User user) {
        return Trainer.builder()
                      .trainingType(new TrainingType(trainerDtoInput.getTrainingTypeId(), "Gym"))
                      .trainees(trainees)
                      .user(user)
                      .build();
    }

    public TrainerDtoOutput createSavedTrainer(Trainer trainer, UserDtoOutput userDtoOutput) {
        return TrainerDtoOutput.builder()
                               .id(trainer.getUser().getId())
                               .trainingType(new TrainingTypeOutputDto(trainer.getTrainingType().getId(),
                                       trainer.getTrainingType().getName()))
                               .traineeIds(
                                       trainer.getTrainees().stream().map(Trainee::getId).collect(Collectors.toList()))
                               .user(userDtoOutput)
                               .build();
    }

    private List<Trainer> createTestTrainers() {
        Trainer trainer1 = Trainer.builder()
                                  .id(1L)
                                  .trainingType(new TrainingType(1L, "Yoga"))
                                  .user(new User(1L, "Antonio", "Lopes", "antonio.lopes", "password1", true, 0))
                                  .trainees(new ArrayList<>())
                                  .build();


        Trainer trainer2 = Trainer.builder()
                                  .id(2L)
                                  .trainingType(new TrainingType(2L, "Box"))
                                  .user(new User(2L, "Hugo", "Boss", "hugo.boss", "password2", true, 0))
                                  .trainees(new ArrayList<>())
                                  .build();

        return List.of(trainer1, trainer2);
    }
}
