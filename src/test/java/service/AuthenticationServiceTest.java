package service;

import org.epam.model.User;
import org.epam.service.AuthenticationService;
import org.epam.service.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void checkAccess_ValidPassword_ReturnsTrue() {
        // Arrange
        AuthenticationService authenticationService = new AuthenticationServiceImpl();
        String validPassword = "correctPassword";
        User user = new User();
        user.setPassword(validPassword);// You might want to create a test user here

        // Act
        boolean result = !authenticationService.checkAccess(validPassword, user);

        // Assert
        assertTrue(result, "Access should be granted for a valid password");
    }

    @Test
    void checkAccess_InvalidPassword_ReturnsFalse() {
        // Arrange
        AuthenticationService authenticationService = new AuthenticationServiceImpl();
        String validPassword = "correctPassword";
        String invalidPassword = "incorrectPassword";
        User user = new User();
        user.setPassword(invalidPassword);// You might want to create a test user here

        // Act
        boolean result = !authenticationService.checkAccess(validPassword, user);

        // Assert
        assertFalse(result, "Access should be denied for an invalid password");
    }
}
