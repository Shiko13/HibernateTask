package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.service.TraineeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/username")
    public TraineeDtoOutput getByUserName(@RequestParam String userName, @RequestParam String password) {
        return traineeService.getByUserName(userName, password);
    }

    @PostMapping()
    public TraineeDtoOutput save(@RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.save(traineeDtoInput);
    }

    @PutMapping("/profile")
    public TraineeDtoOutput updateProfile(@RequestParam String userName, @RequestParam String password,
                                          @RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.updateProfile(userName, password, traineeDtoInput);
    }

    @PutMapping("/trainer-list")
    public TraineeDtoOutput updateTrainerList(@RequestParam String userName, @RequestParam String password,
                                              @RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.updateTrainerList(userName, password, traineeDtoInput);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteByUsername(@RequestParam String userName, @RequestParam String password) {
        traineeService.deleteByUsername(userName, password);
        return ResponseEntity.noContent().build();
    }
}
