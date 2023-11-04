package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.mapper.UserMapper;
import org.epam.model.User;
import org.epam.model.dto.UserDtoInput;
import org.epam.model.dto.UserDtoOutput;
import org.epam.repo.UserRepo;
import org.epam.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Value("${password.length}")
    int passwordLength;

    @Override
    public UserDtoOutput save(UserDtoInput userDtoInput) {
        User user = userRepo.save(createEntireUser(userDtoInput));
        return UserMapper.INSTANCE.toDto(user);
    }

    private User createEntireUser(UserDtoInput userDtoInput) {
        String password = RandomStringGenerator.generateRandomString(passwordLength);
        String baseUserName = userDtoInput.getFirstName().toLowerCase() + "." + userDtoInput.getLastName().toLowerCase();
        String userName = baseUserName;
        int count = 1;

        while (isUserNameExistsInDatabase(userName)) {
            userName = baseUserName + "-" + count;
            count++;
        }

        return User.builder()
                .firstName(userDtoInput.getFirstName())
                .lastName(userDtoInput.getLastName())
                .userName(userName)
                .password(password)
                .isActive(userDtoInput.getIsActive())
                .build();
    }

    private boolean isUserNameExistsInDatabase(String userName) {
        Optional<User> user = userRepo.findByUserName(userName);
        return user.isPresent();
    }
}
