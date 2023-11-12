package controller;

import org.epam.controller.UserController;
import org.epam.model.dto.UserDtoOutput;
import org.epam.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void changePassword_ShouldReturnUserDtoOutput() {
        String userName = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserDtoOutput expectedUserOutput = createUserDtoOutput();

        when(userService.changePassword(userName, oldPassword, newPassword)).thenReturn(expectedUserOutput);

        UserDtoOutput result = userController.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(expectedUserOutput, result);
    }

    @Test
    void switchActivate_ShouldReturnTraineeDtoOutput() {
        String userName = "testUser";
        String password = "testPassword";
        UserDtoOutput expectedOutput = createUserDtoOutput();

        when(userService.switchActivate(userName, password)).thenReturn(expectedOutput);

        UserDtoOutput result = userController.switchActivate(userName, password);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    private UserDtoOutput createUserDtoOutput() {
        return UserDtoOutput.builder()
                            .id(1L)
                            .firstName("John")
                            .lastName("Doe")
                            .userName("john.doe")
                            .isActive(true)
                            .build();
    }
}
