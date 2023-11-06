package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.error.NotFoundException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.UserMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepo traineeRepo;

    private final UserRepo userRepo;

    private final TrainerRepo trainerRepo;

    @Override
    @Transactional
    public TraineeDtoOutput save(TraineeDtoInput traineeDtoInput) {
        log.info("save, traineeDtoInput = {}", traineeDtoInput);
        User user = userRepo.findById(traineeDtoInput.getUserId()).orElseThrow(() -> new NotFoundException("Not found"));

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());
        Trainee traineeToSave = TraineeMapper.INSTANCE.toEntity(traineeDtoInput);
        traineeToSave.setTrainers(selectedTrainers);

        Trainee trainee = traineeRepo.save(traineeToSave);

        return createTraineeDtoOutput(trainee, user);
    }

    @Override
    public TraineeDtoOutput getByUserName(String userName, String password) {
        log.info("getByUserName, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        return createTraineeDtoOutput(trainee, user);
    }

    @Override
    @Transactional
    public TraineeDtoOutput changePassword(String userName, String oldPassword, String newPassword) {
        log.info("changePassword, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(oldPassword, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setPassword(newPassword);
        User updatedUser = userRepo.save(user);

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        return createTraineeDtoOutput(trainee, updatedUser);
    }

    @Override
    @Transactional
    public TraineeDtoOutput updateProfile(String userName, String password, TraineeDtoInput traineeDtoInput) {
        log.info("updateProfile, traineeDtoInput = {}", traineeDtoInput);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        trainee.setAddress(traineeDtoInput.getAddress());
        trainee.setDateOfBirth(traineeDtoInput.getDateOfBirth());
        trainee.setUserId(traineeDtoInput.getUserId());
        Trainee updatedTrainee = traineeRepo.save(trainee);

        return createTraineeDtoOutput(updatedTrainee, user);
    }

    @Override
    @Transactional
    public TraineeDtoOutput updateTrainerList(String userName, String password, TraineeDtoInput traineeDtoInput) {
        log.info("updateTrainerList, traineeDtoInput = {}", traineeDtoInput);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());
        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        trainee.setTrainers(selectedTrainers);
        Trainee updatedTrainee = traineeRepo.save(trainee);

        return createTraineeDtoOutput(updatedTrainee, user);
    }

    @Override
    @Transactional
    public TraineeDtoOutput switchActivate(String userName, String password) {
        log.info("switchActivate, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepo.save(user);

        return createTraineeDtoOutput(trainee, updatedUser);
    }

    @Override
    @Transactional
    public void deleteByUsername(String userName, String password) {
        log.info("deleteByUsername, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        traineeRepo.deleteById(trainee.getId());
        userRepo.deleteById(user.getId());
    }

    private User getUserByUserName(String userName) {
        return userRepo.findByUserName(userName)
                       .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TraineeDtoOutput createTraineeDtoOutput(Trainee trainee, User user) {
        return TraineeDtoOutput.builder()
                               .id(trainee.getId())
                               .dateOfBirth(trainee.getDateOfBirth())
                               .address(trainee.getAddress())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                               .build();
    }

    public boolean authenticate(String password, User user) {
            return !user.getPassword().equals(password);
    }

    private List<Long> convertToTrainerIds(List<Trainer> trainers) {
        return trainers.stream().map(Trainer::getId).collect(Collectors.toList());
    }
}
