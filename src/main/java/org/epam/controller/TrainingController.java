package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainee;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.service.TraineeService;
import org.epam.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<List<TrainingDtoOutput>> findByDateRangeAndTrainee(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String traineeUsername) {
        List<TrainingDtoOutput> filteredTrainings = trainingService.findByDateRangeAndTraineeUserName(startDate, endDate, traineeUsername);
        return new ResponseEntity<>(filteredTrainings, HttpStatus.OK);
    }

    @GetMapping("/criteria-trainer")
    public ResponseEntity<List<TrainingDtoOutput>> findByDateRangeAndTrainer(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String trainerUsername) {
        List<TrainingDtoOutput> filteredTrainings = trainingService.findByDateRangeAndTrainerUserName(startDate, endDate, trainerUsername);
        return new ResponseEntity<>(filteredTrainings, HttpStatus.OK);
    }
    @PostMapping()
    TrainingDtoOutput save(@RequestBody TrainingDtoInput trainingDtoInput) {
        return trainingService.save(trainingDtoInput);
    }
}
