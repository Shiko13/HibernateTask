package org.epam.service;

import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepo trainerRepo;

    private final UserRepo userRepo;

    private final TraineeRepo traineeRepo;

    @Override
    @Transactional
    public TrainerDtoOutput save(TrainerDtoInput trainerDtoInput) {
        User user = userRepo.findById(trainerDtoInput.getUserId()).orElseThrow(() -> new NotFoundException("Not found"));

        List<Trainee> selectedTrainees = traineeRepo.findAllById(trainerDtoInput.getTraineeIds());
        Trainer trainerToSave = TrainerMapper.INSTANCE.toEntity(trainerDtoInput);
        trainerToSave.setTrainees(selectedTrainees);

        Trainer trainer = trainerRepo.save(trainerToSave);

        for (Trainee trainee : selectedTrainees) {
            trainee.getTrainers().add(trainer);
        }

        traineeRepo.saveAll(selectedTrainees);

        return TrainerDtoOutput.builder()
                               .id(trainer.getId())
                               .trainingTypeId(trainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .traineeIds(convertToTraineeIds(trainer.getTrainees()))
                               .build();
    }

    @Override
    public TrainerDtoOutput getByUserName(String userName, String password) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        return TrainerDtoOutput.builder()
                               .id(trainer.getId())
                               .trainingTypeId(trainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .traineeIds(convertToTraineeIds(trainer.getTrainees()))
                               .build();
    }

    @Override
    @Transactional
    public TrainerDtoOutput changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(oldPassword, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setPassword(newPassword);
        User updatedUser = userRepo.save(user);

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        return TrainerDtoOutput.builder()
                               .id(trainer.getId())
                               .trainingTypeId(trainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(updatedUser))
                               .traineeIds(convertToTraineeIds(trainer.getTrainees()))
                               .build();
    }

    @Override
    @Transactional //TODO:delete all relations trainee-trainer
    public TrainerDtoOutput update(String userName, String password, TrainerDtoInput trainerDtoInput) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        List<Trainee> selectedTrainees = traineeRepo.findAllById(trainerDtoInput.getTraineeIds());
        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        trainer.setTrainingTypeId(trainerDtoInput.getTrainingTypeId());
        trainer.setUserId(trainerDtoInput.getUserId());
        trainer.setTrainees(selectedTrainees);
        Trainer updatedTrainer = trainerRepo.save(trainer);

        for (Trainee trainee : selectedTrainees) {
            trainee.getTrainers().add(trainer);
        }

        traineeRepo.saveAll(selectedTrainees);

        return TrainerDtoOutput.builder()
                               .id(updatedTrainer.getId())
                               .trainingTypeId(updatedTrainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .traineeIds(convertToTraineeIds(trainer.getTrainees()))
                               .build();
    }

    @Override
    @Transactional
    public TrainerDtoOutput switchActivate(String userName, String password) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepo.save(user);

        return TrainerDtoOutput.builder()
                               .id(trainer.getId())
                               .trainingTypeId(trainer.getTrainingTypeId())
                               .user(UserMapper.INSTANCE.toDto(updatedUser))
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
