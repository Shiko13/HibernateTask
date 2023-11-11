package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public UserDtoOutput save(@RequestBody UserDtoInput userDtoInput) {
        return userService.save(userDtoInput);
    }

    @PutMapping("/password")
    public UserDtoOutput changePassword(@RequestParam String userName, @RequestParam String oldPassword,
                                           @RequestParam String newPassword) {
        return userService.changePassword(userName, oldPassword, newPassword);
    }

    @PutMapping("/activate")
    public UserDtoOutput switchActivate(@RequestParam String userName, @RequestParam String password) {
        return userService.switchActivate(userName, password);
    }
}
