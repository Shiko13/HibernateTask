package org.epam.service;

import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;

public interface UserService {

    UserDtoOutput save(UserDtoInput userDtoInput);
}
