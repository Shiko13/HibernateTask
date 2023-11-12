package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.error.ErrorMessageConstants;
import org.epam.error.NotFoundException;
import org.epam.mapper.TrainerMapper;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.repo.TraineeRepo;
import org.epam.repo.TrainerRepo;
import org.epam.repo.TrainingTypeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepo trainerRepo;

    private final TraineeRepo traineeRepo;

    private final TrainerMapper trainerMapper;

    private final TrainingTypeRepo trainingTypeRepo;

    private final AuthenticationService authenticationService;

    private final UserService userService;


    @Override
    @Transactional
    public TrainerDtoOutput save(TrainerDtoInput trainerDtoInput) {
        log.info("save, trainerDtoInput = {}", trainerDtoInput);

        User user = userService.save(trainerDtoInput.getUser());
        trainerDtoInput.setId(user.getId());

        List<Trainee> selectedTrainees = traineeRepo.findAllByIdIn(trainerDtoInput.getTraineeIds());
        TrainingType trainingType = trainingTypeRepo.findById(trainerDtoInput.getTrainingTypeId())
                                                    .orElseThrow(() -> new AccessException(
                                                            ErrorMessageConstants.ACCESS_ERROR_MESSAGE));

        Trainer trainerToSave = trainerMapper.toEntity(trainerDtoInput);
        trainerToSave.setTrainingType(trainingType);
        trainerToSave.setTrainees(selectedTrainees);
        trainerToSave.setUser(user);

        Trainer trainer = trainerRepo.save(trainerToSave);
        selectedTrainees.forEach(t -> t.getTrainers().add(trainer));
        traineeRepo.saveAll(selectedTrainees);

        return trainerMapper.toDtoOutput(trainer);
    }

    @Override
    public TrainerDtoOutput getByUserName(String userName, String password) {
        log.info("getByUserName, userName = {}", userName);

        User user = getUserByUserName(userName);
        authenticate(password, user);

        Trainer trainer = trainerRepo.findByUserId(user.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));

        return trainerMapper.toDtoOutput(trainer);
    }

    @Override
    @Transactional
    public TrainerDtoOutput updateProfile(String userName, String password, TrainerDtoInput trainerDtoInput) {
        log.info("updateProfile, userName = {}", userName);

        User user = getUserByUserName(userName);
        authenticate(password, user, trainerDtoInput);

        Trainer trainer = trainerRepo.findByUserId(user.getId())
                                     .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));

        if (!trainerDtoInput.getTrainingTypeId().equals(trainer.getTrainingType().getId())) {
            TrainingType trainingType = trainingTypeRepo.findById(trainerDtoInput.getTrainingTypeId())
                                                        .orElseThrow(() -> new AccessException(
                                                                ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
            trainer.setTrainingType(trainingType);
        }

        Trainer updatedTrainer = trainerRepo.save(trainer);

        return trainerMapper.toDtoOutput(updatedTrainer);
    }

    @Override
    public List<TrainerDtoOutput> getTrainersWithEmptyTrainees() {
        log.info("getTrainersWithEmptyTrainees");

        List<Trainer> trainers = trainerRepo.findByTraineesIsEmptyAndUserIsActiveTrue();

        if (trainers.isEmpty()) {
            return new ArrayList<>();
        }

        return trainers.stream().map(trainerMapper::toDtoOutput).collect(Collectors.toList());
    }

    private User getUserByUserName(String userName) {
        return userService.findUserByUsername(userName)
                          .orElseThrow(() -> new NotFoundException(ErrorMessageConstants.NOT_FOUND_MESSAGE));
    }

    public void authenticate(String password, User user, TrainerDtoInput trainerDtoInput) {
        if (authenticationService.checkAccess(password, user) ||
                !Objects.equals(user.getId(), trainerDtoInput.getId())) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }
    }

    public void authenticate(String password, User user) {
        if (authenticationService.checkAccess(password, user)) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }
    }
}
