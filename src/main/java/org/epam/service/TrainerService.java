package org.epam.service;

import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;

import java.util.List;

public interface TrainerService {

    TrainerDtoOutput save(TrainerDtoInput trainerDtoInput);

    TrainerDtoOutput getByUserName(String userName, String password);

    TrainerDtoOutput changePassword(String userName, String oldPassword, String newPassword);

    TrainerDtoOutput updateProfile(String userName, String password, TrainerDtoInput trainerDtoInput);

    TrainerDtoOutput switchActivate(String userName, String password);

    List<TrainerDtoOutput> getTrainersWithEmptyTrainees();
}
