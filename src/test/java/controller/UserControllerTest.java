package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.controller.UserController;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void save_ShouldReturnUserDtoOutput() throws Exception {
        UserDtoInput userDtoInput = createTestUserDtoInput();
        UserDtoOutput expected = createExpectedUserDtoOutput();

        when(userService.save(userDtoInput)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(asJsonString(userDtoInput)))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.content().json(asJsonString(expected)));
    }

    @Test
    void changePassword_ShouldReturnUserDtoOutput() {
        String userName = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserDtoOutput expectedUserOutput = createExpectedUserDtoOutput();

        when(userService.changePassword(userName, oldPassword, newPassword)).thenReturn(expectedUserOutput);

        UserDtoOutput result = userController.changePassword(userName, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(expectedUserOutput, result);
    }

    @Test
    void switchActivate_ShouldReturnTraineeDtoOutput() {
        String userName = "testUser";
        String password = "testPassword";
        UserDtoOutput expectedOutput = createExpectedUserDtoOutput();

        when(userService.switchActivate(userName, password)).thenReturn(expectedOutput);

        UserDtoOutput result = userController.switchActivate(userName, password);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    private UserDtoInput createTestUserDtoInput() {
        UserDtoInput userDtoInput = new UserDtoInput();
        userDtoInput.setFirstName("John");
        userDtoInput.setLastName("Doe");
        userDtoInput.setIsActive(true);

        return userDtoInput;
    }

    private UserDtoOutput createExpectedUserDtoOutput() {
        UserDtoOutput expected = new UserDtoOutput();
        expected.setId(1L);
        expected.setFirstName("John");
        expected.setLastName("Doe");
        expected.setUserName("john.doe");
        expected.setIsActive(true);

        return expected;
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
