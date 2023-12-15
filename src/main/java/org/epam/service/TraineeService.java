package org.epam.service;

import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;

public interface TraineeService {

    TraineeDtoOutput save(TraineeDtoInput traineeDtoInput);

    TraineeDtoOutput getByUserName(String userName, String password);

    TraineeDtoOutput updateProfile(String userName, String password, TraineeDtoInput traineeDtoInput);

    TraineeDtoOutput updateTrainerList(String userName, String password, TraineeDtoInput traineeDtoInput);

    void deleteByUsername(String userName, String password);
}
