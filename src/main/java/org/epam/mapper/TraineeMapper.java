package org.epam.mapper;

import org.epam.model.Trainee;
import org.epam.model.dto.TraineeDtoInput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TraineeMapper {

    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    TraineeDtoInput toDto(Trainee trainee);

    Trainee toEntity(TraineeDtoInput traineeDtoInput);
}
