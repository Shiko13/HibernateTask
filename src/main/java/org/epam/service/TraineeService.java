package org.epam.service;

import org.epam.model.Trainee;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.springframework.web.bind.annotation.RequestParam;

public interface TraineeService {

    TraineeDtoOutput save(TraineeDtoInput traineeDtoInput);

    TraineeDtoOutput getByUserName(String userName, String password);

    TraineeDtoOutput changePassword(String userName, String oldPassword, String newPassword);

    TraineeDtoOutput update(String userName, String password, TraineeDtoInput traineeDtoInput);

    TraineeDtoOutput switchActivate(String userName, String password);

    void deleteByUsername(String userName, String password);
}
