package org.epam.mapper;

import org.epam.model.Trainer;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    TrainerDtoInput toDto(Trainer trainer);

    Trainer toEntity(TrainerDtoInput trainerDtoInput);

    TrainerDtoOutput toOutputDto(Trainer trainer);
}
