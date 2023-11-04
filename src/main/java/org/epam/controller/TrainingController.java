package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.service.TrainingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;


    @GetMapping("/criteria-trainee")
    public List<TrainingDtoOutput> findByDateRangeAndTrainee(@RequestParam LocalDate startDate,
                                                                             @RequestParam LocalDate endDate,
                                                                             @RequestParam String traineeUsername) {
        return trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername);
    }

    @GetMapping("/criteria-trainer")
    public List<TrainingDtoOutput> findByDateRangeAndTrainer(@RequestParam LocalDate startDate,
                                                                             @RequestParam LocalDate endDate,
                                                                             @RequestParam String trainerUsername) {
        return trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername);
    }

    @PostMapping()
    public TrainingDtoOutput save(@RequestBody TrainingDtoInput trainingDtoInput) {
        return trainingService.save(trainingDtoInput);
    }
}
