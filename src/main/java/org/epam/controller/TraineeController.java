package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    @GetMapping
    Trainee getById(@PathVariable Long id) {
        return null;
    }
}
