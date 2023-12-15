package org.epam.mapper;

import org.epam.model.Training;
import org.epam.model.dto.TrainingDtoInput;
import org.epam.model.dto.TrainingDtoOutput;
import org.epam.model.dto.TrainingShortDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class, TraineeMapper.class, UserMapper.class, TrainingTypeMapper.class})
public interface TrainingMapper {

    TrainingDtoInput toDto(Training training);

    Training toEntity(TrainingDtoInput trainingDtoInput);


    List<TrainingDtoOutput> toDtoList(List<Training> trainings);

    @Mapping(target = "type", source = "trainingType")
    TrainingDtoOutput toDtoOutput(Training training);

    @Mapping(target = "type", source = "trainingType")
    TrainingShortDtoOutput toShortDtoOutput(Training training);
}
