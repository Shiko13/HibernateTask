package service;

import org.epam.error.AccessException;
import org.epam.error.NotFoundException;
import org.epam.mapper.TraineeMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.service.AuthenticationService;
import org.epam.service.TraineeServiceImpl;
import org.epam.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeRepo traineeRepo;

    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    private User user;
    private List<Trainer> selectedTrainers;
    private TraineeDtoInput traineeDtoInput;
    private Trainee traineeToSave;
    private TraineeDtoOutput savedTrainee;

    private UserDtoOutput userDtoOutput;

    @BeforeEach
    void setUp() {
        user = createUser();
        userDtoOutput = createUserDtoOutput(user);
        selectedTrainers = createSelectedTrainers();
        traineeDtoInput = createTraineeDtoInput(user, selectedTrainers);
        traineeToSave = createTraineeToSave(traineeDtoInput, selectedTrainers, user);
        savedTrainee = createSavedTrainee(traineeToSave, userDtoOutput);
    }

    @Test
    void save_shouldReturnSavedTraineeDtoOutput() {
        when(trainerRepo.findAllById(traineeDtoInput.getTrainerIds())).thenReturn(selectedTrainers);
        when(traineeRepo.save(traineeToSave)).thenReturn(traineeToSave);
        when(traineeMapper.toEntity(traineeDtoInput)).thenReturn(traineeToSave);
        when(traineeMapper.toDtoOutput(traineeToSave)).thenReturn(savedTrainee);
        when(userService.save(traineeDtoInput.getUser())).thenReturn(user);

        TraineeDtoOutput result = traineeService.save(traineeDtoInput);

        assertNotNull(result);
        assertEquals(savedTrainee.getId(), result.getId());
        assertEquals(savedTrainee.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(savedTrainee.getAddress(), result.getAddress());
        assertEquals(savedTrainee.getUser(), result.getUser());
        assertEquals(savedTrainee.getTrainerIds(), result.getTrainerIds());
    }

    @Test
    void getByUserName_shouldOk() {
        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(traineeToSave));
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(traineeMapper.toDtoOutput(traineeToSave)).thenReturn(savedTrainee);

        TraineeDtoOutput result = traineeService.getByUserName(user.getUserName(), user.getPassword());

        assertNotNull(result);
        assertEquals(savedTrainee.getId(), result.getId());
        assertEquals(savedTrainee.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(savedTrainee.getAddress(), result.getAddress());
        assertEquals(savedTrainee.getUser(), result.getUser());
        assertEquals(savedTrainee.getTrainerIds(), result.getTrainerIds());
    }

    @Test
    void getByUserName_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        String userName = user.getUserName();
        String password = user.getPassword();

        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.ofNullable(user));

        assertThrows(NotFoundException.class, () -> traineeService.getByUserName(userName, password),
                "A NotFoundException should be thrown.");
    }

    @Test
    void updateProfile_WithValidInput_ShouldReturnUpdatedTraineeDtoOutput() {
        TraineeDtoInput updatedTrainee = createUpdatedTraineeDtoInput(user, selectedTrainers);
        Trainee updatedSavedTrainee = createTraineeToSave(updatedTrainee, selectedTrainers, user);
        TraineeDtoOutput savedUpdatedTrainee = createSavedTrainee(updatedSavedTrainee, userDtoOutput);

        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(traineeToSave));
        when(traineeRepo.save(any(Trainee.class))).thenReturn(updatedSavedTrainee);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(traineeMapper.toDtoOutput(updatedSavedTrainee)).thenReturn(savedUpdatedTrainee);

        TraineeDtoOutput result = traineeService.updateProfile(user.getUserName(), user.getPassword(), updatedTrainee);

        assertNotNull(result);
        assertEquals(updatedTrainee.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatedTrainee.getAddress(), result.getAddress());
        assertEquals(updatedTrainee.getUser().getLastName(), result.getUser().getLastName());
    }

    @Test
    void deleteByUsername_WithValidInput_ShouldDeleteTraineeAndUser() {
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));

        traineeToSave.setId(user.getId());

        assertDoesNotThrow(() -> traineeService.deleteByUsername(user.getUserName(), user.getPassword()));

        verify(traineeRepo).deleteById(traineeToSave.getId());
    }

    @Test
    void updateTrainerList_WithValidInput_ShouldUpdateTrainerList() {
        List<Trainer> updatedTrainers = createUpdatedSelectedTrainers();
        List<Long> updatedTrainerIds = updatedTrainers.stream().map(Trainer::getId).collect(Collectors.toList());

        traineeDtoInput.setTrainerIds(updatedTrainerIds);
        savedTrainee.setTrainerIds(updatedTrainerIds);
        traineeToSave.setTrainers(updatedTrainers);

        when(traineeRepo.findByUserId(user.getId())).thenReturn(Optional.of(traineeToSave));
        when(trainerRepo.findAllById(traineeDtoInput.getTrainerIds())).thenReturn(selectedTrainers);
        when(traineeRepo.save(any(Trainee.class))).thenReturn(traineeToSave);
        when(userService.findUserByUsername(user.getUserName())).thenReturn(Optional.of(user));
        when(traineeMapper.toDtoOutput(traineeToSave)).thenReturn(savedTrainee);

        TraineeDtoOutput result =
                traineeService.updateTrainerList(user.getUserName(), user.getPassword(), traineeDtoInput);

        assertNotNull(result);
        assertEquals(updatedTrainerIds, savedTrainee.getTrainerIds());
    }

    @Test
    void authenticate_ValidPasswordAndMatchingIds_NoExceptionThrown() {
        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        assertDoesNotThrow(() -> traineeService.authenticate(user.getPassword(), user, traineeDtoInput),
                "Exception should not be thrown for valid credentials and matching IDs");

        verify(authenticationService).checkAccess(user.getPassword(), user);
    }

    @Test
    void authenticate_InvalidPassword_AccessExceptionThrown() {
        when(authenticationService.checkAccess("Invalid password", user)).thenReturn(true);

        AccessException exception = assertThrows(AccessException.class,
                () -> traineeService.authenticate("Invalid password", user, traineeDtoInput),
                "An AccessException should be thrown for invalid password");

        verify(authenticationService).checkAccess("Invalid password", user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void authenticate_MismatchedIds_AccessExceptionThrown() {
        String password = user.getPassword();
        traineeDtoInput.setId(user.getId() + 1);

        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        AccessException exception =
                assertThrows(AccessException.class, () -> traineeService.authenticate(password, user, traineeDtoInput),
                        "An AccessException should be thrown for mismatched IDs");

        verify(authenticationService).checkAccess(user.getPassword(), user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void authenticate_ValidPassword_NoExceptionThrown() {
        when(authenticationService.checkAccess(user.getPassword(), user)).thenReturn(false);

        assertDoesNotThrow(() -> traineeService.authenticate(user.getPassword(), user),
                "No exception should be thrown for a valid password");

        verify(authenticationService).checkAccess(user.getPassword(), user);
    }

    @Test
    void authenticate_InvalidPassword_AccessExceptionThrown2() {
        when(authenticationService.checkAccess("Invalid password", user)).thenReturn(true);

        AccessException exception =
                assertThrows(AccessException.class, () -> traineeService.authenticate("Invalid password", user),
                        "An AccessException should be thrown for an invalid password");

        verify(authenticationService).checkAccess("Invalid password", user);

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    public TraineeDtoInput createTraineeDtoInput(User user, List<Trainer> trainers) {
        return TraineeDtoInput.builder()
                              .id(user.getId())
                              .dateOfBirth(LocalDate.of(1985, 5, 7))
                              .address("Common street 53")
                              .user(UserDtoInput.builder()
                                                .firstName(user.getFirstName())
                                                .lastName(user.getLastName())
                                                .isActive(user.getIsActive())
                                                .build())
                              .trainerIds(trainers.stream().map(Trainer::getId).collect(Collectors.toList()))
                              .build();
    }

    public TraineeDtoInput createUpdatedTraineeDtoInput(User user, List<Trainer> trainers) {
        return TraineeDtoInput.builder()
                              .id(user.getId())
                              .dateOfBirth(LocalDate.of(1987, 7, 7))
                              .address("Api street 49")
                              .user(UserDtoInput.builder()
                                                .firstName(user.getFirstName())
                                                .lastName(user.getLastName())
                                                .isActive(user.getIsActive())
                                                .build())
                              .trainerIds(trainers.stream().map(Trainer::getId).collect(Collectors.toList()))
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

    public List<Trainer> createSelectedTrainers() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(2L);

        Trainer trainer2 = new Trainer();
        trainer2.setId(3L);

        return List.of(trainer1, trainer2);
    }

    public List<Trainer> createUpdatedSelectedTrainers() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(4L);

        Trainer trainer2 = new Trainer();
        trainer2.setId(5L);

        return List.of(trainer1, trainer2);
    }

    public Trainee createTraineeToSave(TraineeDtoInput traineeDtoInput, List<Trainer> trainers, User user) {
        return Trainee.builder()
                      .address(traineeDtoInput.getAddress())
                      .dateOfBirth(traineeDtoInput.getDateOfBirth())
                      .trainers(trainers)
                      .user(user)
                      .build();
    }

    public TraineeDtoOutput createSavedTrainee(Trainee trainee, UserDtoOutput userDtoOutput) {
        return TraineeDtoOutput.builder()
                               .id(trainee.getUser().getId())
                               .address(trainee.getAddress())
                               .dateOfBirth(trainee.getDateOfBirth())
                               .trainerIds(
                                       trainee.getTrainers().stream().map(Trainer::getId).collect(Collectors.toList()))
                               .user(userDtoOutput)
                               .build();
    }
}