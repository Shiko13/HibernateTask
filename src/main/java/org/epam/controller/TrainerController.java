package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.service.TrainerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/username")
    public TrainerDtoOutput getByUserName(@RequestParam String userName, @RequestParam String password) {
        return trainerService.getByUserName(userName, password);
    }

    @GetMapping("/free")
    public List<TrainerDtoOutput> getTrainersWithEmptyTrainees() {
        return trainerService.getTrainersWithEmptyTrainees();
    }

    @PostMapping()
    public TrainerDtoOutput save(@RequestBody TrainerDtoInput trainerDtoInput) {
        return trainerService.save(trainerDtoInput);
    }

    @PutMapping("/profile")
    public TrainerDtoOutput updateProfile(@RequestParam String userName, @RequestParam String password,
                                          @RequestBody TrainerDtoInput trainerDtoInput) {
        return trainerService.updateProfile(userName, password, trainerDtoInput);
    }
}
