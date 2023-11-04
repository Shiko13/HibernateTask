package org.epam.service;

import org.epam.model.Trainer;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;

public interface TrainerService {

    TrainerDtoOutput save(TrainerDtoInput trainerDtoInput);

    TrainerDtoOutput getByUserName(String userName, String password);

    TrainerDtoOutput changePassword(String userName, String oldPassword, String newPassword);

    TrainerDtoOutput update(String userName, String password, TrainerDtoInput trainerDtoInput);

    TrainerDtoOutput switchActivate(String userName, String password);
}
