package org.epam.mapper;

import org.epam.model.Trainee;
import org.epam.model.dto.TraineeDtoInput;
import org.epam.model.dto.TraineeDtoOutput;
import org.epam.model.dto.TraineeTrainingShortDtoOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class, UserMapper.class})
public interface TraineeMapper {

    TraineeDtoInput toDto(Trainee trainee);

    @Mapping(target = "id", source = "userId")
    Trainee toEntity(TraineeDtoInput traineeDtoInput);

    @Mapping(target = "trainerIds", source = "trainers")
    TraineeDtoOutput toDtoOutput(Trainee trainee);

    TraineeTrainingShortDtoOutput toShortDtoOutput(Trainee trainee);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    void updateTraineeProfile(@MappingTarget Trainee existingTrainee, TraineeDtoInput traineeDtoInput);

    List<Long> toIds(List<Trainee> trainees);

    default Long toId(Trainee trainee) {
        return trainee.getId();
    }
}
