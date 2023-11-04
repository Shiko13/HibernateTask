package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.service.TraineeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/username")
    TraineeDtoOutput getByUserName(@RequestParam String userName, @RequestParam String password) {
        return traineeService.getByUserName(userName, password);
    }

    @PostMapping()
    TraineeDtoOutput save(@RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.save(traineeDtoInput);
    }

    @PutMapping("/password")
    TraineeDtoOutput changePassword(@RequestParam String userName,
                           @RequestParam String oldPassword,
                           @RequestParam String newPassword) {
        return traineeService.changePassword(userName, oldPassword, newPassword);
    }

    @PutMapping
    TraineeDtoOutput update(@RequestParam String userName,
                                    @RequestParam String password,
                                    @RequestBody TraineeDtoInput traineeDtoInput) {
        return traineeService.update(userName, password, traineeDtoInput);
    }

    @PutMapping("/activate")
    TraineeDtoOutput switchActivate(@RequestParam String userName,
                            @RequestParam String password) {
        return traineeService.switchActivate(userName, password);
    }

    @DeleteMapping()
    ResponseEntity<Void> deleteByUsername(@RequestParam String userName,
                                          @RequestParam String password) {

        traineeService.deleteByUsername(userName, password);
        return ResponseEntity.noContent().build();
    }
}
