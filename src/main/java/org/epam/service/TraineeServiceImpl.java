package org.epam.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepo traineeRepo;

    private final UserRepo userRepo;

    private final TrainerRepo trainerRepo;

    @Override
    @Transactional
    public TraineeDtoOutput save(TraineeDtoInput traineeDtoInput) {
        User user = userRepo.findById(traineeDtoInput.getUserId()).orElseThrow(() -> new NotFoundException("Not found"));

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());
        Trainee traineeToSave = TraineeMapper.INSTANCE.toEntity(traineeDtoInput);
        traineeToSave.setTrainers(selectedTrainers);

        Trainee trainee = traineeRepo.save(traineeToSave);

        return TraineeDtoOutput.builder()
                               .id(trainee.getId())
                               .dateOfBirth(trainee.getDateOfBirth())
                               .address(trainee.getAddress())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                               .build();
    }

    @Override
    public TraineeDtoOutput getByUserName(String userName, String password) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        return TraineeDtoOutput.builder()
                .id(trainee.getId())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .user(UserMapper.INSTANCE.toDto(user))
                .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                .build();
    }

    @Override
    @Transactional
    public TraineeDtoOutput changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(oldPassword, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setPassword(newPassword);
        User updatedUser = userRepo.save(user);

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        return TraineeDtoOutput.builder()
                               .id(trainee.getId())
                               .dateOfBirth(trainee.getDateOfBirth())
                               .address(trainee.getAddress())
                               .user(UserMapper.INSTANCE.toDto(updatedUser))
                               .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                               .build();
    }

    @Override
    @Transactional
    public TraineeDtoOutput update(String userName, String password, TraineeDtoInput traineeDtoInput) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        List<Trainer> selectedTrainers = trainerRepo.findAllById(traineeDtoInput.getTrainerIds());
        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));
        trainee.setAddress(traineeDtoInput.getAddress());
        trainee.setDateOfBirth(traineeDtoInput.getDateOfBirth());
        trainee.setUserId(traineeDtoInput.getUserId());
        trainee.setTrainers(selectedTrainers);
        Trainee updatedTrainee = traineeRepo.save(trainee);

        return TraineeDtoOutput.builder()
                               .id(updatedTrainee.getId())
                               .dateOfBirth(updatedTrainee.getDateOfBirth())
                               .address(updatedTrainee.getAddress())
                               .user(UserMapper.INSTANCE.toDto(user))
                               .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                               .build();
    }

    @Override
    @Transactional
    public TraineeDtoOutput switchActivate(String userName, String password) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepo.save(user);

        return TraineeDtoOutput.builder()
                               .id(trainee.getId())
                               .dateOfBirth(trainee.getDateOfBirth())
                               .address(trainee.getAddress())
                               .user(UserMapper.INSTANCE.toDto(updatedUser))
                               .trainerIds(convertToTrainerIds(trainee.getTrainers()))
                               .build();
    }

    @Override
    @Transactional
    public void deleteByUsername(String userName, String password) {
        User user = userRepo.findByUserName(userName).orElseThrow(() -> new NotFoundException("Not found"));

        if (authenticate(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Not found"));

        traineeRepo.deleteById(trainee.getId());
        userRepo.deleteById(user.getId());
    }

    private boolean authenticate(String password, User user) {
            return !user.getPassword().equals(password);
    }

    private List<Long> convertToTrainerIds(List<Trainer> trainers) {
        return trainers.stream().map(Trainer::getId).collect(Collectors.toList());
    }
}
