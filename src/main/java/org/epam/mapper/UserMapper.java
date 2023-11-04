package org.epam.mapper;

import org.epam.model.User;
import org.epam.model.dto.UserDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDtoOutput toDto(User user);
}
