package org.epam.service;

import org.epam.model.User;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;

import java.util.Optional;

public interface UserService {

    UserDtoOutput save(UserDtoInput userDtoInput);

    UserDtoOutput changePassword(String userName, String oldPassword, String newPassword);

    UserDtoOutput switchActivate(String userName, String password);

    Optional<User> findUserByUsername(String userName);
}
