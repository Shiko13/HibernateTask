package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainer;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.service.TrainerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/username")
    TrainerDtoOutput getByUserName(@RequestParam String userName, @RequestParam String password) {
        return trainerService.getByUserName(userName, password);
    }

    @PostMapping()
    TrainerDtoOutput save(@RequestBody TrainerDtoInput trainerDtoInput) {
        return trainerService.save(trainerDtoInput);
    }

    @PutMapping("/password")
    TrainerDtoOutput changePassword(@RequestParam String userName,
                                    @RequestParam String oldPassword,
                                    @RequestParam String newPassword) {
        return trainerService.changePassword(userName, oldPassword, newPassword);
    }

    @PutMapping
    TrainerDtoOutput update(@RequestParam String userName,
                            @RequestParam String password,
                            @RequestBody TrainerDtoInput trainerDtoInput) {
        return trainerService.update(userName, password, trainerDtoInput);
    }

    @PutMapping("/activate")
    TrainerDtoOutput switchActivate(@RequestParam String userName,
                                    @RequestParam String password) {
        return trainerService.switchActivate(userName, password);
    }
}
