package org.epam.mapper;

import org.epam.model.Training;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    TrainingDtoInput toDto(Training training);

    Training toEntity(TrainingDtoInput trainingDtoInput);

    List<TrainingDtoOutput> toDtoList(List<Training> trainings);

    TrainingDtoOutput toDtoOutput(Training training);
}
