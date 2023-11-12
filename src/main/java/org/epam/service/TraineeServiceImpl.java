package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.error.ErrorMessageConstants;
import org.epam.error.NotFoundException;
import org.epam.mapper.TraineeMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepo traineeRepo;

    private final TrainerRepo trainerRepo;

    private final TraineeMapper traineeMapper;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @Override
    @Transactional
    public TraineeDtoOutput save(TraineeDtoInput traineeDtoInput) {
        log.info("save, traineeDtoInput = {}", traineeDtoInput);

        User user = userService.save(traineeDtoInput.getUser());
        traineeDtoInput.setId(user.getId());

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());

        Trainee traineeToSave = traineeMapper.toEntity(traineeDtoInput);
        traineeToSave.setTrainers(selectedTrainers);
        traineeToSave.setUser(user);

        Trainee trainee = traineeRepo.save(traineeToSave);

        return traineeMapper.toDtoOutput(trainee);
    }

    @Override
    @Transactional
    public TraineeDtoOutput getByUserName(String userName, String password) {
        log.info("getByUserName, userName = {}", userName);

        User user = getUserByUserName(userName);
        authenticate(password, user);

        Trainee trainee = traineeRepo.findByUserId(user.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));

        return traineeMapper.toDtoOutput(trainee);
    }

    @Override
    @Transactional
    public TraineeDtoOutput updateProfile(String userName, String password, TraineeDtoInput traineeDtoInput) {
        log.info("updateProfile, traineeDtoInput = {}", traineeDtoInput);

        User user = getUserByUserName(userName);
        authenticate(password, user, traineeDtoInput);

        Trainee trainee = traineeRepo.findByUserId(user.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));
        traineeMapper.updateTraineeProfile(trainee, traineeDtoInput);

        Trainee updatedTrainee = traineeRepo.save(trainee);

        return traineeMapper.toDtoOutput(updatedTrainee);
    }

    @Override
    @Transactional
    public TraineeDtoOutput updateTrainerList(String userName, String password, TraineeDtoInput traineeDtoInput) {
        log.info("updateTrainerList, traineeDtoInput = {}", traineeDtoInput);

        User user = getUserByUserName(userName);
        authenticate(password, user, traineeDtoInput);

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());
        Trainee trainee = traineeRepo.findByUserId(user.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));
        trainee.setTrainers(selectedTrainers);

        Trainee updatedTrainee = traineeRepo.save(trainee);

        return traineeMapper.toDtoOutput(updatedTrainee);
    }

    @Override
    @Transactional
    public void deleteByUsername(String userName, String password) {
        log.info("deleteByUsername, userName = {}", userName);

        User user = getUserByUserName(userName);
        authenticate(password, user);

        traineeRepo.deleteById(user.getId());
    }

    private User getUserByUserName(String userName) {
        return userService.findUserByUsername(userName)
                          .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
    }

    public void authenticate(String password, User user, TraineeDtoInput traineeDtoInput) {
        if (authenticationService.checkAccess(password, user) ||
                !Objects.equals(user.getId(), traineeDtoInput.getId())) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }
    }

    public void authenticate(String password, User user) {
        if (authenticationService.checkAccess(password, user)) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }
    }
}
