package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.error.NotFoundException;
import org.epam.mapper.TrainerMapper;
import org.epam.mapper.UserMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepo trainerRepo;

    private final UserRepo userRepo;

    private final TraineeRepo traineeRepo;

    @Override
    @Transactional
    public TrainerDtoOutput save(TrainerDtoInput trainerDtoInput) {
        log.info("save, trainerDtoInput = {}", trainerDtoInput);
        User user =
                userRepo.findById(trainerDtoInput.getUserId()).orElseThrow(() -> new NotFoundException("Not found"));

        List<Trainee> selectedTrainees = traineeRepo.findAllById(trainerDtoInput.getTraineeIds());
        Trainer trainerToSave = TrainerMapper.INSTANCE.toEntity(trainerDtoInput);
        trainerToSave.setTrainees(selectedTrainees);

        Trainer trainer = trainerRepo.save(trainerToSave);

        for (Trainee trainee : selectedTrainees) {
            trainee.getTrainers().add(trainer);
        }

        traineeRepo.saveAll(selectedTrainees);

        return createTrainerDtoOutput(trainer, user);
    }

    @Override
    public TrainerDtoOutput getByUserName(String userName, String password) {
        log.info("getByUserName, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        return createTrainerDtoOutput(trainer, user);
    }

    @Override
    @Transactional
    public TrainerDtoOutput changePassword(String userName, String oldPassword, String newPassword) {
        log.info("changePassword, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(oldPassword, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setPassword(newPassword);
        User updatedUser = userRepo.save(user);

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        return createTrainerDtoOutput(trainer, updatedUser);
    }

    @Override
    @Transactional
    public TrainerDtoOutput updateProfile(String userName, String password, TrainerDtoInput trainerDtoInput) {
        log.info("updateProfile, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        trainer.setTrainingTypeId(trainerDtoInput.getTrainingTypeId());
        trainer.setUserId(trainerDtoInput.getUserId());
        Trainer updatedTrainer = trainerRepo.save(trainer);

        return createTrainerDtoOutput(updatedTrainer, user);
    }

    @Override
    @Transactional
    public TrainerDtoOutput switchActivate(String userName, String password) {
        log.info("switchActivate, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepo.save(user);

        return createTrainerDtoOutput(trainer, updatedUser);
    }

    @Override
    public List<TrainerDtoOutput> getTrainersWithEmptyTrainees() {
        log.info("getTrainersWithEmptyTrainees");
        List<Trainer> trainers = trainerRepo.findTrainersWithEmptyTraineesList();

        if (trainers.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> userIds = trainers.stream().map(Trainer::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap =
                userRepo.findAllById(userIds).stream().collect(Collectors.toMap(User::getId, user -> user));

        List<TrainerDtoOutput> trainerDtoOutputs = new ArrayList<>();

        for (Trainer trainer : trainers) {
            trainerDtoOutputs.add(createTrainerDtoOutput(trainer, userMap.get(trainer.getUserId())));
        }

        return trainerDtoOutputs;
    }

    private User getUserByUserName(String userName) {
        return userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TrainerDtoOutput createTrainerDtoOutput(Trainer trainer, User user) {
        return TrainerDtoOutput.builder()
                               .id(trainer.getId())
                               .trainingTypeId(trainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .traineeIds(convertToTraineeIds(trainer.getTrainees()))
                               .build();
    }

    private boolean authenticate(String password, User user) {
        return !user.getPassword().equals(password);
    }

    private List<Long> convertToTraineeIds(List<Trainee> trainees) {
        return trainees.stream().map(Trainee::getId).collect(Collectors.toList());
    }
}
