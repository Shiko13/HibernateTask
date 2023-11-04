package org.epam.mapper;

import org.epam.model.Trainee;
import org.epam.model.User;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TraineeMapper {

    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    TraineeDtoInput toDto(Trainee trainee);

    Trainee toEntity(TraineeDtoInput traineeDtoInput);

//    @Mapping(target = "user", source = "trainee.user")
//    TraineeDtoOutput toDtoOutput(Trainee trainee, User user);
}
