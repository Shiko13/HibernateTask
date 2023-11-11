package service;

import org.epam.error.AccessException;
import org.epam.mapper.UserMapper;
import org.epam.model.User;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.UserRepo;
import org.epam.service.AuthenticationService;
import org.epam.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationService authenticationService;

    private UserDtoInput userDtoInput;

    private User savedUser;

    private UserDtoOutput userDtoOutput;

    @BeforeEach
    void setUp() {
        userDtoInput = createTestUserDtoInput();
        savedUser = createTestUser(userDtoInput);
        userDtoOutput = createTestUserDtoOutput(savedUser);
    }

    @Test
    void save_shouldCreateAndReturnUserDtoOutput() {
        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userDtoOutput);

        UserDtoOutput result = userService.save(userDtoInput);

        assertNotNull(result);
        assertEquals(userDtoInput.getFirstName(), result.getFirstName());
        assertEquals(userDtoInput.getLastName(), result.getLastName());
        assertEquals(userDtoInput.getIsActive(), result.getIsActive());
        assertEquals(userDtoOutput.getUserName(), result.getUserName());
    }

    @Test
    void createEntireUser_shouldGenerateUniqueUserName() {
        User user = userService.createEntireUser(userDtoInput);

        assertNotNull(user);
        assertTrue(user.getUserName()
                       .startsWith(userDtoInput.getFirstName().toLowerCase() + "." +
                               userDtoInput.getLastName().toLowerCase()));
    }

    @Test
    void createEntireUser_shouldUseBaseUserNameIfUnique() {
        User user = userService.createEntireUser(userDtoInput);

        assertNotNull(user);
        assertEquals(savedUser.getUserName(), user.getUserName());
    }

    @Test
    void isUserNameExistsInDatabase_shouldReturnTrueIfUserNameExists() {
        when(userRepo.existsByUserName(savedUser.getUserName())).thenReturn(true);

        boolean userNameExists = userService.isUserNameExistsInDatabase(savedUser.getUserName());

        assertTrue(userNameExists);
    }

    @Test
    void isUserNameExistsInDatabase_shouldReturnFalseIfUserNameDoesNotExist() {
        when(userRepo.existsByUserName(savedUser.getUserName())).thenReturn(false);

        boolean userNameExists = userService.isUserNameExistsInDatabase(savedUser.getUserName());

        assertFalse(userNameExists);
    }

    @Test
    void switchActivate_WithIncorrectPassword_ShouldThrowAccessException() {
        when(userRepo.findByUserName(savedUser.getUserName())).thenReturn(Optional.of(savedUser));
        when(authenticationService.checkAccess("Incorrect password", savedUser)).thenReturn(true);

        AccessException exception = assertThrows(AccessException.class,
                () -> userService.switchActivate(savedUser.getUserName(), "Incorrect password"));
        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void switchActivate_WithValidInput_ShouldReturnUpdatedTraineeDtoOutput() {
        when(userRepo.findByUserName(savedUser.getUserName())).thenReturn(Optional.of(savedUser));
        when(authenticationService.checkAccess(savedUser.getPassword(), savedUser)).thenReturn(false);
        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        userDtoOutput.setIsActive(!userDtoOutput.getIsActive());
        when(userMapper.toDto(savedUser)).thenReturn(userDtoOutput);

        UserDtoOutput result = userService.switchActivate(savedUser.getUserName(), savedUser.getPassword());

        assertNotNull(result);
        assertFalse(result.getIsActive());
    }

    @Test
    void switchActivate_InvalidPassword_ThrowsAccessException() {
        String userName = savedUser.getUserName();
        String invalidPassword = "Invalid password";

        when(userRepo.findByUserName(userName)).thenReturn(java.util.Optional.of(savedUser));
        when(authenticationService.checkAccess(invalidPassword, savedUser)).thenReturn(true);

        AccessException exception =
                assertThrows(AccessException.class, () -> userService.switchActivate(userName, invalidPassword));

        verify(userRepo).findByUserName(userName);
        verify(authenticationService).checkAccess(invalidPassword, savedUser);
        verify(userRepo, never()).save(any(User.class));

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void changePassword_ValidOldPassword_SuccessfullyChanged() {
        User updatedUser = createTestUser(userDtoInput);
        updatedUser.setPassword("New password");

        when(userRepo.findByUserName(savedUser.getUserName())).thenReturn(java.util.Optional.of(savedUser));
        when(authenticationService.checkAccess(savedUser.getPassword(), savedUser)).thenReturn(false);
        when(userRepo.save(savedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(userDtoOutput);

        UserDtoOutput result =
                userService.changePassword(savedUser.getUserName(), savedUser.getPassword(), updatedUser.getPassword());

        verify(userRepo).findByUserName(savedUser.getUserName());
        verify(userRepo).save(any(User.class));
        verify(userMapper).toDto(any(User.class));

        assertNotNull(result);
        assertEquals(userDtoOutput, result);
    }

    @Test
    void changePassword_InvalidOldPassword_ThrowsAccessException() {
        User userWithInvalidPassword = createTestUser(userDtoInput);
        userWithInvalidPassword.setPassword("Invalid password");

        when(userRepo.findByUserName(savedUser.getUserName())).thenReturn(java.util.Optional.of(savedUser));
        when(authenticationService.checkAccess(savedUser.getPassword(), savedUser)).thenReturn(true);

        AccessException exception = assertThrows(AccessException.class,
                () -> userService.changePassword(savedUser.getUserName(), savedUser.getPassword(),
                        userWithInvalidPassword.getPassword()));

        verify(userRepo).findByUserName(savedUser.getUserName());
        verify(authenticationService).checkAccess(savedUser.getPassword(), savedUser);
        verify(userRepo, never()).save(any(User.class));
        verify(userMapper, never()).toDto(any(User.class));

        assertEquals("You don't have access for this.", exception.getMessage());
    }

    @Test
    void findUserByUsername_WithValidUserName_ShouldReturnUser() {
        String validUserName = savedUser.getUserName();
        when(userRepo.findByUserNameAndPrefix(eq(validUserName), eq(0))).thenReturn(Optional.of(savedUser));

        Optional<User> result = userService.findUserByUsername(validUserName);

        assertTrue(result.isPresent());
        assertEquals(savedUser, result.get());
    }

    @Test
    void findUserByUsername_WithUserNameWithPrefix_ShouldReturnUser() {
        String userNameWithPrefix = savedUser.getUserName() + "-1";
        User expectedUser = createTestUser(userDtoInput);
        expectedUser.setUserName(userNameWithPrefix);

        when(userRepo.findByUserNameAndPrefix(eq(savedUser.getUserName()), eq(1))).thenReturn(
                Optional.of(expectedUser));

        Optional<User> result = userService.findUserByUsername(userNameWithPrefix);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }


    private UserDtoInput createTestUserDtoInput() {
        return UserDtoInput.builder().firstName("John").lastName("Doe").isActive(true).build();
    }

    @Test
    void findUserByUsername_WithInvalidUserName_ShouldReturnEmpty() {
        String invalidUserName = "nonexistent.user";
        when(userRepo.findByUserNameAndPrefix(eq(invalidUserName), eq(0))).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserByUsername(invalidUserName);

        assertTrue(result.isEmpty());
    }

    @Test
    void createEntireUser_WhenUserNameDoesNotExist_ShouldReturnUserWithZeroPrefix() {
        when(userService.isUserNameExistsInDatabase(savedUser.getUserName())).thenReturn(false);

        User resultUser = userService.createEntireUser(userDtoInput);

        assertNotNull(resultUser);
        assertEquals(savedUser.getUserName(), resultUser.getUserName());
        assertEquals(0, resultUser.getPrefix());
    }

    @Test
    void createEntireUser_WhenUserNameExistsWithPrefix_ShouldReturnUserWithIncrementedPrefix() {
        when(userService.isUserNameExistsInDatabase(savedUser.getUserName())).thenReturn(true);
        when(userRepo.findMaxPrefixByUsername(savedUser.getUserName())).thenReturn(2);

        User resultUser = userService.createEntireUser(userDtoInput);

        assertNotNull(resultUser);
        assertEquals(savedUser.getUserName(), resultUser.getUserName());
        assertEquals(3, resultUser.getPrefix());
    }

    private User createTestUser(UserDtoInput userDtoInput) {
        return User.builder()
                   .id(1L)
                   .firstName(userDtoInput.getFirstName())
                   .lastName(userDtoInput.getLastName())
                   .userName(userDtoInput.getFirstName().toLowerCase() + "." + userDtoInput.getLastName().toLowerCase())
                   .isActive(userDtoInput.getIsActive())
                   .password("password")
                   .prefix(0)
                   .build();
    }

    private UserDtoOutput createTestUserDtoOutput(User user) {
        return UserDtoOutput.builder()
                            .id(1L)
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .userName(user.getUserName())
                            .isActive(user.getIsActive())
                            .build();
    }
}
