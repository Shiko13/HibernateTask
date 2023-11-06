package service;

import org.epam.model.User;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.UserRepo;
import org.epam.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Test
    void save_shouldCreateAndReturnUserDtoOutput() {
        UserDtoInput userDtoInput = createTestUserDtoInput();

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserDtoOutput result = userService.save(userDtoInput);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(true, result.getIsActive());
    }

    @Test
    void createEntireUser_shouldGenerateUniqueUserName() {
        UserDtoInput userDtoInput = createTestUserDtoInput();
        userDtoInput.setFirstName("John");
        userDtoInput.setLastName("Doe");

        when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(createTestUser())).thenReturn(Optional.empty());

        User user = userService.createEntireUser(userDtoInput);

        assertNotNull(user);
        assertTrue(user.getUserName().startsWith("john.doe"));
    }

    @Test
    void createEntireUser_shouldUseBaseUserNameIfUnique() {
        UserDtoInput userDtoInput = createTestUserDtoInput();
        userDtoInput.setFirstName("John");
        userDtoInput.setLastName("Doe");

        when(userRepo.findByUserName(anyString())).thenReturn(Optional.empty());

        User user = userService.createEntireUser(userDtoInput);

        assertNotNull(user);
        assertEquals("john.doe", user.getUserName());
    }

    @Test
    void isUserNameExistsInDatabase_shouldReturnTrueIfUserNameExists() {
        when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(createTestUser()));

        boolean userNameExists = userService.isUserNameExistsInDatabase("john.doe");

        assertTrue(userNameExists);
    }

    @Test
    void isUserNameExistsInDatabase_shouldReturnFalseIfUserNameDoesNotExist() {
        when(userRepo.findByUserName(anyString())).thenReturn(Optional.empty());

        boolean userNameExists = userService.isUserNameExistsInDatabase("john.doe");

        assertFalse(userNameExists);
    }

    private UserDtoInput createTestUserDtoInput() {
        UserDtoInput userDtoInput = new UserDtoInput();
        userDtoInput.setFirstName("John");
        userDtoInput.setLastName("Doe");
        userDtoInput.setIsActive(true);

        return userDtoInput;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("john.doe");
        user.setIsActive(true);

        return user;
    }
}
