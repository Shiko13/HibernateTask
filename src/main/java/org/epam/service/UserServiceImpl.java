package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.error.AccessException;
import org.epam.mapper.UserMapper;
import org.epam.model.User;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.UserRepo;
import org.epam.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    @Value("${password.length}")
    private int passwordLength;

    @Override
    @Transactional
    public UserDtoOutput save(UserDtoInput userDtoInput) {
        log.info("save, userDtoInput = {}", userDtoInput);
        User user = userRepo.save(createEntireUser(userDtoInput));

        return userMapper.toDto(user);
    }

    @Override
    public UserDtoOutput changePassword(String userName, String oldPassword, String newPassword) {
        log.info("changePassword, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticationService.checkAccess(oldPassword, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setPassword(newPassword);
        return userMapper.toDto(userRepo.save(user));
    }

    @Override
    public UserDtoOutput switchActivate(String userName, String password) {
        log.info("switchActivate, userName = {}", userName);

        User user = getUserByUserName(userName);
        if (authenticationService.checkAccess(password, user)) {
            throw new AccessException("You don't have access for this.");
        }

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepo.save(user);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public Optional<User> findUserByUsername(String userName) {
        if (userName.matches(".+-\\d$")) {
            String[] parts = userName.split("-");
            String userNameWithPrefix = parts[0].trim();
            Integer prefix = Integer.valueOf(parts[1]);
            return userRepo.findByUserNameAndPrefix(userNameWithPrefix, prefix);
        } else {
            return userRepo.findByUserNameAndPrefix(userName, 0);
        }
    }

    private User getUserByUserName(String userName) {
        return userRepo.findByUserName(userName)
                       .orElseThrow(() -> new AccessException("You don't have access for this."));
    }


    public User createEntireUser(UserDtoInput userDtoInput) {
        String password = RandomStringGenerator.generateRandomString(passwordLength);
        String userName = userDtoInput.getFirstName().toLowerCase() + "." + userDtoInput.getLastName().toLowerCase();
        Integer maxPrefix = 0;

        if (isUserNameExistsInDatabase(userName)) {
            maxPrefix = userRepo.findMaxPrefixByUsername(userName);
            maxPrefix++;
        }

        return User.builder()
                .firstName(userDtoInput.getFirstName())
                .lastName(userDtoInput.getLastName())
                .userName(userName)
                .password(password)
                .isActive(userDtoInput.getIsActive())
                .prefix(maxPrefix)
                .build();

    }

    public boolean isUserNameExistsInDatabase(String userName) {
        return userRepo.existsByUserName(userName);
    }
}
