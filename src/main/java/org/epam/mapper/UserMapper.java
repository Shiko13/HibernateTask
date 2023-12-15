package org.epam.mapper;

import org.epam.model.User;
import org.epam.model.dto.UserDtoOutput;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDtoOutput toDto(User user);

    default String appendPrefix(String userName, int prefix) {
        return (prefix != 0) ? userName + "-" + prefix : userName;
    }

    @AfterMapping
    default void appendPrefix(@MappingTarget UserDtoOutput userDtoOutput, User user) {
        userDtoOutput.setUserName(appendPrefix(user.getUserName(), user.getPrefix()));
    }
}
