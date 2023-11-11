package org.epam.mapper;

import org.epam.model.Trainer;
import org.epam.model.dto.TrainerDtoInput;
import org.epam.model.dto.TrainerDtoOutput;
import org.epam.model.dto.TrainerTrainingShortDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TraineeMapper.class, UserMapper.class, TrainingTypeMapper.class})
public interface TrainerMapper {

    TrainerDtoInput toDto(Trainer trainer);

    @Mapping(target = "id", source = "userId")
    Trainer toEntity(TrainerDtoInput trainerDtoInput);

    @Mapping(target = "traineeIds", source = "trainees")
    TrainerDtoOutput toDtoOutput(Trainer trainer);

    TrainerTrainingShortDtoOutput toShortDtoOutput(Trainer trainer);

    List<Long> toIds(List<Trainer> trainers);

    default Long toId(Trainer trainer) {
        return trainer.getId();
    }
}
